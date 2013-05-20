package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.EstrategiaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.EstrategiaService;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.HTMLTableUtil;
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
    private ProdutoEdicaoService produtoEdicaoService;

    @Autowired
    private EstudoService estudoService;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private RoteiroService roteiroService;

    @Autowired
    private EstoqueProdutoService estoqueProdutoService;

    @Autowired
    private EstrategiaService estrategiaService;

    @Autowired
    private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Autowired
    private DefinicaoBases definicaoBases;

    @Path("index")
    public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId, String juramentado, String suplementar,
	    String lancado, String promocional, String sobra, Long repDistrib, ProdutoDistribuicaoVO produtoDistribuicaoVO) {

	Estudo estudo = null;
	ProdutoEdicao produtoEdicao = null;

	if (estudoId != null && estudoId != 0l) {
	    estudo = estudoService.obterEstudo(estudoId);
	    result.include("estudo", estudo);
	    produtoEdicao = estudo.getProdutoEdicao();
	} else {
	    produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicao.toString());
	}

	session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, null);
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, null);

	EstoqueProduto estoqueProdutoEdicao = estoqueProdutoService.buscarEstoquePorProduto(produtoEdicao.getId());

	Lancamento lancamento = null;
	if (lancamentoId == null) {
	    lancamento = findLancamentoBalanceado(produtoEdicao);
	} else {
	    lancamento = lancamentoService.obterLancamentoNaMesmaSessao(lancamentoId);
	}
	List<ProdutoEdicaoVendaMediaDTO> selecionados = new ArrayList<>();
	Estrategia estrategia = estrategiaService.buscarPorProdutoEdicao(produtoEdicao);
	EstrategiaDTO estrat = new EstrategiaDTO();
	if (estrategia != null) {
	    BeanUtils.copyProperties(estrategia, estrat);

	    for (EdicaoBaseEstrategia base : estrategia.getBasesEstrategia()) {
		selecionados.addAll(distribuicaoVendaMediaRepository.pesquisar(base.getProdutoEdicao().getProduto().getCodigo(), null, base
			.getProdutoEdicao().getNumeroEdicao()));
	    }
	} else {
	    EstudoTransient estudoTemp = new EstudoTransient();
	    ProdutoEdicaoEstudo prod = new ProdutoEdicaoEstudo(codigoProduto);
	    prod.setNumeroEdicao(edicao);
	    estudoTemp.setProdutoEdicaoEstudo(prod);
	    try {
		definicaoBases.executar(estudoTemp);

		for (ProdutoEdicaoEstudo base : estudoTemp.getEdicoesBase()) {
		    selecionados.addAll(distribuicaoVendaMediaRepository.pesquisar(base.getProduto().getCodigo(), null, base.getNumeroEdicao()));
		}
	    } catch (Exception e) {
		System.out.println("erro: "+ e.getMessage());
	    }
	}
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);

	List<Roteiro> roteiros = roteiroService.buscarTodos();

	result.include("componentes", ComponentesPDV.values());
	result.include("roteiros", roteiros);
	result.include("juramentado", juramentado);

	if (estoqueProdutoEdicao != null) {
	    result.include("suplementar", estoqueProdutoEdicao.getQtdeSuplementar());
	} else {
	    result.include("suplementar", suplementar);
	}

	if (lancado != null) {
	    result.include("lancado", lancado);
	} else {
	    result.include("lancado", lancamento.getReparte());
	}

	if (promocional != null) {
	    result.include("promocional", promocional);	
	} else {
	    result.include("promocional", lancamento.getRepartePromocional());
	}

	if (sobra != null) {
	    result.include("sobra", sobra);
	} else {
	    result.include("sobra", estudo.getSobra());
	}

	if (repDistrib != null) {
	    result.include("repDistrib", repDistrib);
	} else {
	    result.include("repDistrib", estudo.getReparteDistribuir());
	}


	result.include("lancamento", lancamento);
	result.include("estrategia", estrat);
	ProdutoEdicaoDTO convertido = converterResultado(produtoEdicao, lancamento);
	// produtoEdicaoRepository.findReparteEVenda(convertido);

	result.include("produtoEdicao", convertido);
	
	
	session.setAttribute(ProdutoDistribuicaoVO.class.getName(), produtoDistribuicaoVO);
	
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

	if (indexes != null) {
	    for (Integer index : indexes) {
		ProdutoEdicaoVendaMediaDTO produtoEdicao = resultadoPesquisa.get(index);
		if (!selecionados.contains(produtoEdicao)) {
		    selecionados.add(produtoEdicao);
		}
	    }
	}
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
	result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
    }

    @Path("gerarEstudo")
    @Post
    public void gerarEstudo(DistribuicaoVendaMediaDTO distribuicaoVendaMedia, String codigoProduto, Long numeroEdicao) throws Exception {
	EstudoTransient estudo = null;
	ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(codigoProduto);
	produto.setNumeroEdicao(numeroEdicao);
	estudo = estudoAlgoritmoService.gerarEstudoAutomatico(distribuicaoVendaMedia, produto, distribuicaoVendaMedia.getReparteDistribuir(), this.getUsuarioLogado());
	String htmlEstudo = HTMLTableUtil.estudoToHTML(estudo);
	result.use(Results.json()).from(htmlEstudo, "estudo").recursive().serialize();
    
	removeItensDuplicadosMatrizDistribuicao();
    
    }
    
    private void removeItensDuplicadosMatrizDistribuicao() {
    	
    	ProdutoDistribuicaoVO vo = (ProdutoDistribuicaoVO)session.getAttribute(ProdutoDistribuicaoVO.class.getName());
    	MatrizDistribuicaoController matrizDistribuicaoController = new MatrizDistribuicaoController();
    	matrizDistribuicaoController.setSession(session);
    	matrizDistribuicaoController.removeItemListaDeItensDuplicadosNaSessao(vo.getIdLancamento(), vo.getIdCopia());
		session.removeAttribute(ProdutoDistribuicaoVO.class.getName());
    }
    
    public HttpSession getSession() {
	return session;
    }

    public void setSession(HttpSession session) {
	this.session = session;
    }
}
