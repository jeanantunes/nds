package br.com.abril.nds.controllers.distribuicao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

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
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO)
	public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId,
			String juramentado, String suplementar, String lancado, String promocional, String sobra){
		if(codigoProduto == null){
			result.nothing();
			return;
		}
		Produto produto = produtoRepository.obterProdutoPorCodigo(codigoProduto);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorProdutoEEdicaoOuNome(produto.getId(), edicao, produto.getNomeComercial());
		Estudo estudo = estudoRepository.buscarPorId(estudoId);
		result.include("estudo", estudo);
		Lancamento lancamento = null;
		if(lancamentoId == null){
			lancamento = findLancamentoBalanceado(produtoEdicao);
		}else{
			lancamento = lancamentoRepository.buscarPorId(lancamentoId);
		}
		
		result.include("juramentado", juramentado);
		result.include("suplementar", suplementar);
		result.include("lancado", lancado);
		result.include("promocional", promocional);
		result.include("sobra", sobra);
		
		result.include("lancamento", lancamento);
		result.include("produtoEdicao", produtoEdicao);
	}
	
	public void pesquisarProdutosEdicao(String codigo, String nome, Long edicao){
		List<ProdutoEdicao> resultado = produtoEdicaoRepository.pesquisar(codigo, nome, edicao);
		
		List<ProdutoEdicaoDTO> convertido = converterResultado(resultado);
		
		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, resultado);
		
		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}
	
	private List<ProdutoEdicaoDTO> converterResultado(List<ProdutoEdicao> resultado){
		List<ProdutoEdicaoDTO> convertido = new ArrayList<ProdutoEdicaoDTO>();
		for(ProdutoEdicao produtoEdicao : resultado){
			ProdutoEdicaoDTO dto = converterResultado(produtoEdicao);
			convertido.add(dto);
		}
		return convertido;
	}
	
	private ProdutoEdicaoDTO converterResultado(ProdutoEdicao produtoEdicao){
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		
		dto.setId(produtoEdicao.getId());
		dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		//TODO Periodo
		Lancamento lancamento = findLancamentoBalanceado(produtoEdicao);
		if(lancamento == null && ! produtoEdicao.getLancamentos().isEmpty()){
			lancamento = produtoEdicao.getLancamentos().iterator().next();
		}
		if(lancamento != null){
			dto.setDataLancamento(lancamento.getDataLancamentoDistribuidor());
			dto.setStatusSituacao(lancamento.getStatus());
		}else{
			dto.setDataLancamentoFormatada("");
		}
		if(produtoEdicao.getReparteDistribuido() == null){
			dto.setRepartePrevisto(BigInteger.ZERO);
		}else{
			dto.setRepartePrevisto(produtoEdicao.getReparteDistribuido());
		}
		
		//TODO Venda
		//TODO Classificação;
		
		return dto;
	}
	
	private Lancamento findLancamentoBalanceado(ProdutoEdicao produtoEdicao){
		for(Lancamento lancamento : produtoEdicao.getLancamentos()){
			if( StatusLancamento.BALANCEADO.equals( lancamento.getStatus() ) ){
				return lancamento;
			}
		}
		return null;
	}
	
	public void adicionarProdutoEdicaoABase(List<Integer> indexes){
		List<ProdutoEdicao> resultadoPesquisa = (List<ProdutoEdicao>) session.getAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO);
		List<ProdutoEdicao> selecionados = (List<ProdutoEdicao>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
		if(selecionados == null){
			selecionados = new ArrayList<ProdutoEdicao>();
		}
		//TODO Verificar os que já foram adicionados pelo ID
		for(Integer index : indexes){
			ProdutoEdicao produtoEdicao = resultadoPesquisa.get(index);
			selecionados.add(produtoEdicao);
		}
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
		
		List<ProdutoEdicaoDTO> convertido = converterResultado(selecionados);

		result.use(Results.json()).withoutRoot().from(convertido).recursive().serialize();
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
}
