package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstrategiaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * @author william.borba
 * 
 */
@Path("/distribuicaoVendaMedia")
@Resource
public class DistribuicaoVendaMediaController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(DistribuicaoVendaMediaController.class);

	public static final String SELECIONADOS_PRODUTO_EDICAO_BASE = "selecionados-produto-edicao-base";

	public static final String RESULTADO_PESQUISA_PRODUTO_EDICAO = "resultado-pesquisa-produto-edicao";

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private EstudoRepository estudoRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private RoteiroRepository roteiroRepository;

	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;

	@Autowired
	private EstrategiaRepository estrategiaRepository;

	@Autowired
	private EstudoAlgoritmoService estudoAlgoritmoService;

	@Path("index")
	@Post
	@Transactional(readOnly = true)
	public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId, String juramentado, String suplementar, String lancado,
			String promocional, String sobra) {
		if (codigoProduto == null) {
			result.nothing();
			return;
		}
		Produto produto = produtoRepository.obterProdutoPorCodigo(codigoProduto);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorProdutoEEdicaoOuNome(produto.getId(), edicao,
				produto.getNomeComercial());
		if (estudoId != null) {
			Estudo estudo = estudoRepository.buscarPorId(estudoId);
			result.include("estudo", estudo);
		}
		Lancamento lancamento = null;
		if (lancamentoId == null) {
			lancamento = findLancamentoBalanceado(produtoEdicao);
		} else {
			lancamento = lancamentoRepository.buscarPorId(lancamentoId);
		}
		Estrategia estrategia = estrategiaRepository.buscarPorProdutoEdicao(produtoEdicao);

		List<Roteiro> roteiros = roteiroRepository.buscarTodos();

		result.include("componentes", ComponentesPDV.values());
		result.include("roteiros", roteiros);
		result.include("juramentado", juramentado);
		result.include("suplementar", suplementar);
		result.include("lancado", lancado);
		result.include("promocional", promocional);
		result.include("sobra", sobra);

		result.include("lancamento", lancamento);
		result.include("estrategia", estrategia);
		ProdutoEdicaoDTO convertido = converterResultado(produtoEdicao, lancamento);
		// produtoEdicaoRepository.findReparteEVenda(convertido);

		result.include("produtoEdicao", convertido);
	}

	@Path("pesquisarProdutosEdicao")
	@Post
	@Transactional(readOnly = true)
	public void pesquisarProdutosEdicao(String codigo, String nome, Long edicao) {
		List<ProdutoEdicao> resultado = produtoEdicaoRepository.pesquisar(codigo, nome, edicao);

		List<ProdutoEdicaoDTO> convertido = converterResultado(resultado);

		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, resultado);

		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}

	private List<ProdutoEdicaoDTO> converterResultado(List<ProdutoEdicao> resultado) {
		List<ProdutoEdicaoDTO> convertido = new ArrayList<ProdutoEdicaoDTO>();
		for (ProdutoEdicao produtoEdicao : resultado) {
			ProdutoEdicaoDTO dto = converterResultado(produtoEdicao, null);
			convertido.add(dto);
		}

		// produtoEdicaoRepository.findReparteEVenda(convertido);

		return convertido;
	}

	private ProdutoEdicaoDTO converterResultado(ProdutoEdicao produtoEdicao, Lancamento lancamento) {
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();

		dto.setId(produtoEdicao.getId());
		dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());

		dto.setNomeComercial(produtoEdicao.getProduto().getNomeComercial());
		dto.setNomeProduto(produtoEdicao.getProduto().getNome());
		dto.setCodigoProduto(produtoEdicao.getProduto().getCodigo());

		dto.setPeriodicidade(produtoEdicao.getProduto().getPeriodicidade());

		if (lancamento == null) {
			lancamento = findLancamentoBalanceado(produtoEdicao);
		}
		if (lancamento != null) {
			dto.setDataLancamento(lancamento.getDataLancamentoDistribuidor());
			dto.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoDistribuidor());
			dto.setStatusSituacao(lancamento.getStatus());
		} else {
			dto.setDataLancamentoFormatada("");
			dto.setDataRecolhimentoDistribuidorFormatada("");
		}

		dto.setPrecoVenda(produtoEdicao.getPrecoVenda());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		if (produtoEdicao.getProduto().getTipoSegmentoProduto() != null) {
			dto.setSegmentacao(produtoEdicao.getProduto().getTipoSegmentoProduto().getDescricao());
		}
		if (produtoEdicao.getProduto().getTipoClassificacaoProduto() != null) {
			dto.setClassificacao(produtoEdicao.getProduto().getTipoClassificacaoProduto().getDescricao());
		}

		return dto;
	}

	@SuppressWarnings("unused")
	private EstoqueProdutoCota findEstoqueProdutoCota(ProdutoEdicao produtoEdicao) {
		return null;
	}

	private Lancamento findLancamentoBalanceado(ProdutoEdicao produtoEdicao) {
		for (Lancamento lancamento : produtoEdicao.getLancamentos()) {
			if (StatusLancamento.BALANCEADO.equals(lancamento.getStatus())) {
				return lancamento;
			}
		}
		return null;
	}

	@Path("removerProdutoEdicaoDaBase")
	@Post
	public void removerProdutoEdicaoDaBase(List<Integer> indexes) {
		@SuppressWarnings("unchecked")
		List<ProdutoEdicao> selecionados = (List<ProdutoEdicao>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		List<ProdutoEdicao> toRemove = new ArrayList<ProdutoEdicao>();
		for (Integer index : indexes) {
			toRemove.add(selecionados.get(index));
		}

		selecionados.removeAll(toRemove);

		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		List<ProdutoEdicaoDTO> convertido = converterResultado(selecionados);
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}

	@Path("adicionarProdutoEdicaoABase")
	@Post
	public void adicionarProdutoEdicaoABase(List<Integer> indexes) {
		@SuppressWarnings("unchecked")
		List<ProdutoEdicao> resultadoPesquisa = (List<ProdutoEdicao>) session.getAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO);
		@SuppressWarnings("unchecked")
		List<ProdutoEdicao> selecionados = (List<ProdutoEdicao>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		if (selecionados == null) {
			selecionados = new ArrayList<ProdutoEdicao>();
		}

		for (Integer index : indexes) {
			ProdutoEdicao produtoEdicao = resultadoPesquisa.get(index);
			if (!selecionados.contains(produtoEdicao)) {
				selecionados.add(produtoEdicao);
			}
		}
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);

		List<ProdutoEdicaoDTO> convertido = converterResultado(selecionados);
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}

	@Path("gerarEstudo")
	@Post
	public void gerarEstudo(DistribuicaoVendaMediaDTO distribuicaoVendaMedia, String codigoProduto) {
		//FIXME o que retornar para a tela após estudo?
		EstudoTransient estudo = null;
		try {
			estudo = estudoAlgoritmoService.gerarEstudoAutomatico(distribuicaoVendaMedia, new ProdutoEdicaoEstudo(codigoProduto), 
					distribuicaoVendaMedia.getReparteDistribuir(), this.getUsuarioLogado());
		} catch (Exception e) {
			log.error("Erro na geração do estudo.", e);
    		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		result.use(Results.json()).from(estudo).recursive().serialize();
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
}
