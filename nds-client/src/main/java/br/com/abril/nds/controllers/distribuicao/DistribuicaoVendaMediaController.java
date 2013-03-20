package br.com.abril.nds.controllers.distribuicao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.caelum.vraptor.Path;
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

	public static final String SELECIONADOS_PRODUTO_EDICAO_BASE = "selecionados-produto-edicao-base";

	public static final String RESULTADO_PESQUISA_PRODUTO_EDICAO = "resultado-pesquisa-produto-edicao";

	@Autowired()
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
	
	@Path("/")
	@Transactional(readOnly=true)
	public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId,
			String juramentado, String suplementar, String lancado, String promocional, String sobra){
		if(codigoProduto == null){
			result.nothing();
			return;
		}
		Produto produto = produtoRepository.obterProdutoPorCodigo(codigoProduto);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorProdutoEEdicaoOuNome(produto.getId(), edicao, produto.getNomeComercial());
		if(estudoId != null){
			//TODO Verificar a necessidade de criar algo no else
			Estudo estudo = estudoRepository.buscarPorId(estudoId);
			result.include("estudo", estudo);
		}
		Lancamento lancamento = null;
		if(lancamentoId == null){
			lancamento = findLancamentoBalanceado(produtoEdicao);
		}else{
			lancamento = lancamentoRepository.buscarPorId(lancamentoId);
		}
		
		List<Roteiro> roteiros = roteiroRepository.buscarTodos();
		
		result.include("componentes", ComponentesPDV.values());
		result.include("roteiros", roteiros);
		result.include("juramentado", juramentado);
		result.include("suplementar", suplementar);
		result.include("lancado", lancado);
		result.include("promocional", promocional);
		result.include("sobra", sobra);
		
		result.include("lancamento", lancamento);
		
		ProdutoEdicaoDTO convertido = converterResultado(produtoEdicao, lancamento);
//		produtoEdicaoRepository.findReparteEVenda(convertido);
		
		result.include("produtoEdicao", convertido);
	}
	
	@Transactional(readOnly=true)
	public void pesquisarProdutosEdicao(String codigo, String nome, Long edicao){
		List<ProdutoEdicao> resultado = null; 
//				produtoEdicaoRepository.pesquisar(codigo, nome, edicao);
		
		List<ProdutoEdicaoDTO> convertido = converterResultado(resultado);
		
		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, resultado);
		
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}
	
	private List<ProdutoEdicaoDTO> converterResultado(List<ProdutoEdicao> resultado){
		List<ProdutoEdicaoDTO> convertido = new ArrayList<ProdutoEdicaoDTO>();
		for(ProdutoEdicao produtoEdicao : resultado){
			ProdutoEdicaoDTO dto = converterResultado(produtoEdicao, null);
			convertido.add(dto);
		}
		
//		produtoEdicaoRepository.findReparteEVenda(convertido);
		
		return convertido;
	}
	
	private ProdutoEdicaoDTO converterResultado(ProdutoEdicao produtoEdicao, Lancamento lancamento){
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		
		dto.setId(produtoEdicao.getId());
		dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		dto.setNomeComercial(produtoEdicao.getProduto().getNomeComercial());
		dto.setNomeProduto(produtoEdicao.getProduto().getNome());
		dto.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		
		dto.setPeriodicidade(produtoEdicao.getProduto().getPeriodicidade());
		
		if(lancamento == null){
			lancamento = findLancamentoBalanceado(produtoEdicao);
		}
		if(lancamento != null){
			dto.setDataLancamento(lancamento.getDataLancamentoDistribuidor());
			dto.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoDistribuidor());
			dto.setStatusSituacao(lancamento.getStatus());
		}else{
			dto.setDataLancamentoFormatada("");
			dto.setDataRecolhimentoDistribuidorFormatada("");
		}
		
		dto.setPrecoVenda(produtoEdicao.getPrecoVenda());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		if(produtoEdicao.getProduto().getTipoSegmentoProduto() != null){
			dto.setSegmentacao(produtoEdicao.getProduto().getTipoSegmentoProduto().getDescricao());
		}
		if(produtoEdicao.getProduto().getTipoClassificacaoProduto() != null){
			dto.setClassificacao(produtoEdicao.getProduto().getTipoClassificacaoProduto().getDescricao());
		}
		
		return dto;
	}
	
	private EstoqueProdutoCota findEstoqueProdutoCota(ProdutoEdicao produtoEdicao){
		return null;
	}
	
	private Lancamento findLancamentoBalanceado(ProdutoEdicao produtoEdicao){
		for(Lancamento lancamento : produtoEdicao.getLancamentos()){
			if( StatusLancamento.BALANCEADO.equals( lancamento.getStatus() ) ){
				return lancamento;
			}
		}
		return null;
	}
	
	public void removerProdutoEdicaoDaBase(List<Integer> indexes){
		List<ProdutoEdicao> selecionados = (List<ProdutoEdicao>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		List<ProdutoEdicao> toRemove = new ArrayList<ProdutoEdicao>();
		for(Integer index : indexes){
			toRemove.add(selecionados.get(index));
		}
		
		selecionados.removeAll(toRemove);
		
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		List<ProdutoEdicaoDTO> convertido = converterResultado(selecionados);
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}
	
	public void adicionarProdutoEdicaoABase(List<Integer> indexes){
		List<ProdutoEdicao> resultadoPesquisa = (List<ProdutoEdicao>) session.getAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO);
		List<ProdutoEdicao> selecionados = (List<ProdutoEdicao>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		if(selecionados == null){
			selecionados = new ArrayList<ProdutoEdicao>();
		}

		for(Integer index : indexes){
			ProdutoEdicao produtoEdicao = resultadoPesquisa.get(index);
			if( ! selecionados.contains(produtoEdicao)){
				selecionados.add(produtoEdicao);
			}
		}
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		
		List<ProdutoEdicaoDTO> convertido = converterResultado(selecionados);
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}
	
	public void teste(DistribuicaoVendaMediaDTO dto){
		System.out.println(dto);
	}
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
}
