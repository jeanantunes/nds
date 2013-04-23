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
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
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
import br.com.caelum.vraptor.Validator;
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
	private Validator validator;

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
	private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;

	@Autowired
	private EstudoAlgoritmoService estudoAlgoritmoService;

	@Path("index")
	@Post
	@Transactional(readOnly = true)
	public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId, String juramentado, String suplementar,
		String lancado, String promocional, String sobra, Long repDistrib) {
		if (codigoProduto == null) {
			result.nothing();
			return;
		}
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicao);
		if (estudoId != null) {
			Estudo estudo = estudoRepository.buscarPorId(estudoId);
			result.include("estudo", estudo);
		}
		Lancamento lancamento = null;
		if (lancamentoId == null) {
			lancamento = findLancamentoBalanceado(produtoEdicao);
		} else {
			lancamento = lancamentoRepository.buscarPorIdSemEstudo(lancamentoId);
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
		result.include("repDistrib", repDistrib);

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
		List<ProdutoEdicaoVendaMediaDTO> resultado = distribuicaoVendaMediaRepository.pesquisar(codigo, nome, edicao);

		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, resultado);
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
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
		List<ProdutoEdicaoVendaMediaDTO> selecionados = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		List<ProdutoEdicaoVendaMediaDTO> toRemove = new ArrayList<ProdutoEdicaoVendaMediaDTO>();
		for (Integer index : indexes) {
			toRemove.add(selecionados.get(index));
		}
		selecionados.removeAll(toRemove);
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
	}

	@Path("adicionarProdutoEdicaoABase")
	@Post
	public void adicionarProdutoEdicaoABase(List<Integer> indexes) {
		List<ProdutoEdicaoVendaMediaDTO> resultadoPesquisa = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO);
		List<ProdutoEdicaoVendaMediaDTO> selecionados = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		if (selecionados == null) {
			selecionados = new ArrayList<ProdutoEdicaoVendaMediaDTO>();
		}

		for (Integer index : indexes) {
			ProdutoEdicaoVendaMediaDTO produtoEdicao = resultadoPesquisa.get(index);
			if (!selecionados.contains(produtoEdicao)) {
				selecionados.add(produtoEdicao);
			}
		}
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
	}

    @Path("gerarEstudo")
    @Post
    public void gerarEstudo(DistribuicaoVendaMediaDTO distribuicaoVendaMedia, String codigoProduto, Long numeroEdicao) throws Exception {
	EstudoTransient estudo = null;
//	try {
	    ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(codigoProduto);
	    produto.setNumeroEdicao(numeroEdicao);
	    estudo = estudoAlgoritmoService.gerarEstudoAutomatico(distribuicaoVendaMedia, produto, distribuicaoVendaMedia.getReparteDistribuir(),
		    this.getUsuarioLogado());
	    // result.use(Results.json()).from(, "result").serialize();
	    result.use(Results.json()).from(estudo.getId(), "result").serialize();
//	} catch (Exception e) {
//	    log.error("Erro na geração do estudo.", e);
//	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
//	}
//	validator.onErrorForwardTo(MatrizDistribuicaoController.class).index();
    }

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
}
