package br.com.abril.nds.controllers.distribuicao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.EstrategiaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.service.DistribuicaoVendaMediaService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.EstrategiaService;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ProdutoEdicaoAlgoritimoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.HTMLTableUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicaoVendaMedia")
@Resource
public class DistribuicaoVendaMediaController extends BaseController {

    public static final String SELECIONADOS_PRODUTO_EDICAO_BASE = "selecionados-produto-edicao-base";

    public static final String RESULTADO_PESQUISA_PRODUTO_EDICAO = "resultado-pesquisa-produto-edicao";
    
    public static final String SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO = "resultado-pesquisa-produto-edicao-base-veranenio";

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
    private DistribuicaoVendaMediaService distribuicaoVendaMediaService;

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Autowired
    private DefinicaoBases definicaoBases;

    @Autowired
    private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
    
    @Autowired
    private ProdutoEdicaoAlgoritimoService produtoEdicaoAlgoritimoService;
    
    @Autowired
    private ProdutoService prodService;

    @Autowired
    private EstudoProdutoEdicaoBaseRepository estudoProdutoEdicaoBaseRepository;
    
    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;
    
    private static final int QTD_MAX_PRODUTO_EDICAO = 6;

    @Path("index")
    public void index(String codigoProduto, Long edicao, Long estudoId, Long lancamentoId, String juramentado, String suplementar,
	    String lancado, String promocional, String sobra, Long repDistrib, ProdutoDistribuicaoVO produtoDistribuicaoVO) {

	EstudoGerado estudo = null;
	ProdutoEdicao produtoEdicao = null;
    Produto produto;

	if (estudoId != null && estudoId != 0l) {
	    estudo = estudoService.obterEstudo(estudoId);
	    result.include("estudo", estudo);
	    result.include("idEstudo", estudo.getId());
	    result.include("statusEstudo", estudo.getStatus().getDescricao());
	    result.include("vendaMediaDTO", estudo.getDadosVendaMedia());
	    produtoEdicao = estudo.getProdutoEdicao();
        produto = estudo.getProdutoEdicao().getProduto();
        lancamentoId = estudo.getLancamentoID();
    } else {
    	result.include("statusEstudo", " ");
    	result.include("idEstudo", " ");
        produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicao.toString());
        produto = produtoEdicao.getProduto();
    }

	session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, null);
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, null);
	session.removeAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO);

	EstoqueProduto estoqueProdutoEdicao = estoqueProdutoService.buscarEstoquePorProduto(produtoEdicao.getId());

	Lancamento lancamento;
	
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
	    selecionados.clear();

	    for (EdicaoBaseEstrategia base : estrategia.getBasesEstrategia()) {
                selecionados.addAll(distribuicaoVendaMediaService.pesquisar(base.getProdutoEdicao().getProduto()
                        .getCodigo(), null, base.getProdutoEdicao().getNumeroEdicao(), base.getProdutoEdicao()
                        .getTipoClassificacaoProduto() != null ? base.getProdutoEdicao().getTipoClassificacaoProduto()
                        .getId() : null, false, false));
	    }
    } else if (estudo != null) {
        List<EdicaoBaseEstudoDTO> edicaoBaseEstudoDTOs = estudoProdutoEdicaoBaseRepository.obterEdicoesBase(estudo.getId());
        for (EdicaoBaseEstudoDTO edicaoBaseEstudoDTO : edicaoBaseEstudoDTOs) {
                selecionados.addAll(distribuicaoVendaMediaService.pesquisar(edicaoBaseEstudoDTO.getCodigoProduto(),
                        edicaoBaseEstudoDTO.getNomeProduto(), edicaoBaseEstudoDTO.getNumeroEdicao().longValue(), null, false, edicaoBaseEstudoDTO.isParcialConsolidado()));        
                }
    } else {
    	
        EstudoTransient estudoTemp = new EstudoTransient();
        estudoTemp.setProdutoEdicaoEstudo(produtoEdicaoAlgoritimoService.getProdutoEdicaoEstudo(
                produto.getCodigo(), produtoEdicao.getNumeroEdicao(), lancamento != null ? lancamento.getId() : null));
        
        definicaoBases.executar(estudoTemp);
        selecionados.clear();
        
        if (estudoTemp.getEdicoesBase() != null && !estudoTemp.getEdicoesBase().isEmpty()) {
        	
    		for (ProdutoEdicaoEstudo base : estudoTemp.getEdicoesBase()) {
    		    if (base.isParcial()) {
    		    	
    		    	List<ProdutoEdicaoVendaMediaDTO> produtosBase;
    		    	
	    		    	if(estudoTemp.getProdutoEdicaoEstudo().getPeriodo() != null && estudoTemp.getProdutoEdicaoEstudo().getPeriodo() > 1) {
	    		    		
	    		    		produtosBase = distribuicaoVendaMediaService.pesquisar(base.getProduto().getCodigo(), base.getProduto().getNome(), base.getNumeroEdicao(), base.getTipoClassificacaoProduto().getId(), false, false);
	    		    	} else {
	    		    		
	    		    		produtosBase = distribuicaoVendaMediaService.pesquisar(base.getProduto().getCodigo(), base.getProduto().getNome(), base.getNumeroEdicao(), base.getTipoClassificacaoProduto().getId(), false, base.isParcial());
	    		    	}
    		    	
	    		    	for(ProdutoEdicaoVendaMediaDTO pevm : produtosBase) {
	    		    		pevm.setIndicePeso(base.getIndicePeso());
	    		    	}
    		    	
    		    	verifyExistAndAdd(selecionados, produtosBase);
    		    } else {
    		    	
    		    	List<ProdutoEdicaoVendaMediaDTO> produtosBase = distribuicaoVendaMediaService.pesquisar(base.getProduto().getCodigo(), null, base.getNumeroEdicao(), 
                            base.getTipoClassificacaoProduto() != null ? base.getTipoClassificacaoProduto().getId() : null, false, base.isParcial());
    		    	
    		    	for(ProdutoEdicaoVendaMediaDTO pevm : produtosBase) {
    		    		pevm.setIndicePeso(base.getIndicePeso());
    		    	}
    		    	
    		        selecionados.addAll(produtosBase);
    		        
    		    }
    		}
    		
    		Collections.sort(selecionados, new Comparator<ProdutoEdicaoVendaMediaDTO>() {

				@Override
				public int compare(ProdutoEdicaoVendaMediaDTO o1, ProdutoEdicaoVendaMediaDTO o2) {
					if(o1.getDataLancamento().getTime() > o2.getDataLancamento().getTime()) {
						return -1;
					}
					return 0;
				}

    			
    		});
    		
    		final boolean parcialComMaisDeUmPeriodo = 
        			(estudoTemp.getProdutoEdicaoEstudo().getPeriodo() != null &&estudoTemp.getProdutoEdicaoEstudo().getPeriodo() > 1);
        	
    		if(!parcialComMaisDeUmPeriodo){
        		
        		if(estudoTemp.isPracaVeraneio() 
        				&& estudoAlgoritmoService.validaPeriodoVeraneio(estudoTemp.getProdutoEdicaoEstudo().getDataLancamento())
        				&& !estudoTemp.getProdutoEdicaoEstudo().isColecao()) {
        			
    	        	List<ProdutoEdicaoEstudo> edicoesPenultimoVeraneio = estudoAlgoritmoService.obterEdicoesPenultimoVeraneio(estudoTemp);
    	        	List<ProdutoEdicaoEstudo> edicoesUltimoVeraneio = estudoAlgoritmoService.obterEdicoesUltimoVeraneio(estudoTemp);
    	        	
    	        	boolean edicoesVeraneioRepartePositivo = false;
    	        	List<ProdutoEdicaoEstudo> edicoesVeraneio = new ArrayList<ProdutoEdicaoEstudo>();
    	        	edicoesVeraneio.addAll(edicoesPenultimoVeraneio);
    	        	edicoesVeraneio.addAll(edicoesUltimoVeraneio);
    	        	
    	        	for(ProdutoEdicaoEstudo pee : edicoesVeraneio) {
    	        		if(pee.getReparte() != null && pee.getReparte().compareTo(BigDecimal.ZERO) > 0) {
    	        			edicoesVeraneioRepartePositivo = true;
    	        			break;
    	        		}
    	        	}
    	        	
    	        	if((edicoesVeraneioRepartePositivo && ((edicoesPenultimoVeraneio != null && !edicoesPenultimoVeraneio.isEmpty()) 
    	        			|| (edicoesUltimoVeraneio != null && !edicoesUltimoVeraneio.isEmpty())))) {
    	        		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO, true);
    	        	} else {
    	        		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO, false);
    	        	}
    	        }
        	}
        }
	}
	
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
	
	session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, selecionados);

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
	} else if (lancamento != null) {
        result.include("lancado", lancamento.getReparte());
    }

	if (promocional != null) {
	    result.include("promocional", promocional);	
	} else if (lancamento != null) {
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

	result.include("produtoEdicao", convertido);

	carregarComboClassificacao();

	session.setAttribute(ProdutoDistribuicaoVO.class.getName(), produtoDistribuicaoVO);
    
    String modoAnalise = "NORMAL";
    
    PeriodoLancamentoParcial periodo = lancamento.getPeriodoLancamentoParcial();
    
    if (periodo != null && periodo.getNumeroPeriodo() > 1) {
    
        modoAnalise = "PARCIAL";
    }
    
    result.include("modoAnalise", modoAnalise);
    }

	private void verifyExistAndAdd(List<ProdutoEdicaoVendaMediaDTO> selecionados, List<ProdutoEdicaoVendaMediaDTO> produtosBase) {
		
		boolean contains = false;
		
		for (ProdutoEdicaoVendaMediaDTO prodBase : produtosBase) {
		    for (ProdutoEdicaoVendaMediaDTO prodEdSelecionado : selecionados) {
		    	if((prodEdSelecionado.getIdLancamento().equals(prodBase.getIdLancamento()))){
		    		contains = true;	    		        			
				}
			}
		   
			if(!contains){
		    	selecionados.add(prodBase);
		    }
		}
	}

    @Path("pesquisarProdutosEdicao")
    @Post
    public void pesquisarProdutosEdicao(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, String modoAnalise, Long idProdutoEdicao, String sortorder, String sortname, int page, int rp) {
    	
    	filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
    	filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroEdicaoBaseDistribuicaoVendaMedia.OrdemColuna.values(), sortname));
		
    	Long idProdutoEdicaoPesquisa = null;
    	
    	if (filtro.getCodigo() != null && filtro.getEdicao() != null) {
    	    
    	    idProdutoEdicaoPesquisa = this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(filtro.getCodigo(), filtro.getEdicao().toString()).getId();
    	}
    	
    	filtro.setConsolidado(modoAnalise.equals("NORMAL") || idProdutoEdicaoPesquisa == null || !idProdutoEdicao.equals(idProdutoEdicaoPesquisa));
    	
    	Produto produto = prodService.obterProdutoPorCodigo(filtro.getCodigo());
    	filtro.setCodigo(produto.getCodigoICD());
    	
        List<ProdutoEdicaoVendaMediaDTO> resultado = distribuicaoVendaMediaService.pesquisar(filtro);
        
		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, resultado);
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
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
			dto.setStatusSituacao(lancamento.getStatus().getDescricao());
		}else{
			dto.setDataLancamentoFormatada("");
		}
		
		dto.setPrecoVenda(produtoEdicao.getPrecoVenda());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		if(produtoEdicao.getProduto().getTipoSegmentoProduto() != null){
		}
		if(produtoEdicao.getTipoClassificacaoProduto() != null){
		}
		
		dto.setPrecoVenda(produtoEdicao.getPrecoVenda());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		if (produtoEdicao.getProduto().getTipoSegmentoProduto() != null) {
		    dto.setSegmentacao(produtoEdicao.getProduto().getTipoSegmentoProduto().getDescricao());
		}
		if (produtoEdicao.getTipoClassificacaoProduto() != null) {
		    dto.setClassificacao(produtoEdicao.getTipoClassificacaoProduto().getDescricao());
		}
	
		return dto;
    }

    private Lancamento findLancamentoBalanceado(ProdutoEdicao produtoEdicao) {
	for (Lancamento lancamento : produtoEdicao.getLancamentos()) {
	    if (StatusLancamento.BALANCEADO.equals(lancamento.getStatus())) {
		return lancamento;
	    }
	}
	return null;
    }

    @Post
    public void removerTodasEdicoesDeBase() {
	List<ProdutoEdicaoVendaMediaDTO> selecionados = new ArrayList<>();
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
	result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
    }

    @SuppressWarnings("unchecked")
	@Path("removerProdutoEdicaoDaBase")
    @Post
    public void removerProdutoEdicaoDaBase(List<Integer> indexes) {
	List<ProdutoEdicaoVendaMediaDTO> selecionados = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
	List<ProdutoEdicaoVendaMediaDTO> toRemove = new ArrayList<>();
	for (Integer index : indexes) {
	    toRemove.add(selecionados.get(index));
	}
	selecionados.removeAll(toRemove);
	session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
	result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
    }

    @SuppressWarnings("unchecked")
	@Path("adicionarProdutoEdicaoABase")
    @Post
    public void adicionarProdutoEdicaoABase(List<Integer> indexes) {
    	
    	List<ProdutoEdicaoVendaMediaDTO> resultadoPesquisa = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO);

		if ((resultadoPesquisa != null) && (resultadoPesquisa.size() > 0)) {

			List<ProdutoEdicaoVendaMediaDTO> selecionados = (List<ProdutoEdicaoVendaMediaDTO>) session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE);
			if (selecionados == null) {
			    selecionados = new ArrayList<>();
			}

			if ((indexes != null) && (indexes.size() > 0)) {
			    for (Integer index : indexes) {
					if (index != null) {
					    ProdutoEdicaoVendaMediaDTO produtoEdicao = resultadoPesquisa.get(index);
					    if (!selecionados.contains(produtoEdicao)) {
					    	selecionados.add(produtoEdicao);
					    }
					}
			    }
			}

			session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, selecionados);
			result.use(Results.json()).withoutRoot().from(selecionados).recursive().serialize();
		} else {
			
			result.nothing();
		}
    }

    @Post
    public void existeBaseVeraneio() {
    	
    	if(session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO) != null) {
    		result.use(Results.json()).from(session.getAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE_VERANEIO), "existeBaseVeraneio").recursive().serialize();
    	} else {
    		result.nothing();
    	}
    }
    
    @Path("gerarEstudo")
    @Post
    public void gerarEstudo(DistribuicaoVendaMediaDTO distribuicaoVendaMedia, String codigoProduto, Long numeroEdicao, Long idLancamento, String dataLancamento) throws Exception {
	
		EstudoTransient estudo = null;
		int qtdEdicoesAbertas = 0;
		Long qtdEstudoParaLancamento = 0L;
		Date datalctoFormatada;
		
		if (distribuicaoVendaMedia.getBases().size() > QTD_MAX_PRODUTO_EDICAO) {
	
	            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não pode ter mais do que "
	                + QTD_MAX_PRODUTO_EDICAO + " bases."));
		}
	
		for (int i = 0; i < distribuicaoVendaMedia.getBases().size(); i++) {
		    ProdutoEdicaoDTO produtoEdicaoDTO = distribuicaoVendaMedia.getBases().get(i);
	
		    if ((!produtoEdicaoDTO.getStatus().equalsIgnoreCase("FECHADO")) && (!produtoEdicaoDTO.getStatus().equalsIgnoreCase("RECOLHIDO") && (!produtoEdicaoDTO.getStatus().equalsIgnoreCase("EM_RECOLHIMENTO")))) {
		    	qtdEdicoesAbertas++;
		    }
		}
	
		if (qtdEdicoesAbertas > 1) {
	        throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Não é possível utilizar mais que uma edição base aberta."));
		}
		
		ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(codigoProduto);
		produto.setNumeroEdicao(numeroEdicao);
		produto.setIdLancamento(idLancamento);
		
		try {
			datalctoFormatada = new SimpleDateFormat("dd/MM/yyyy").parse(dataLancamento);
		} catch (Exception e) {
	        throw new Exception("Data de lançamento em formato incorreto.");
		}
		
		produto.setDataLancamento(datalctoFormatada);
		
		qtdEstudoParaLancamento = estudoService.countEstudosPorLancamento(idLancamento, datalctoFormatada);
		
		if(qtdEstudoParaLancamento >= 3){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Este lançamento já possui o máximo de 3 estudos gerados."));
		}
		
		estudo = estudoAlgoritmoService.gerarEstudoAutomatico(distribuicaoVendaMedia, produto, distribuicaoVendaMedia.getReparteDistribuir(), this.getUsuarioLogado());
		
		this.matrizDistribuicaoService.atualizarPercentualAbrangencia(estudo.getId());
		
	    estudoService.gravarDadosVendaMedia(estudo.getId(), distribuicaoVendaMedia);
	        
	    estudoService.criarRepartePorPDV(estudo.getId());
	        
		String htmlEstudo = HTMLTableUtil.estudoToHTML(estudo);
	
		List<Object> response = new ArrayList<>();
		response.add(htmlEstudo);
		response.add(estudo.getId());
		response.add(estudo.isLiberado() == null ? false : true);
		
		session.setAttribute(SELECIONADOS_PRODUTO_EDICAO_BASE, null);
		session.setAttribute(RESULTADO_PESQUISA_PRODUTO_EDICAO, null);
		
		result.use(Results.json()).from(response).recursive().serialize();
	
		removeItensDuplicadosMatrizDistribuicao();

    }

    private void removeItensDuplicadosMatrizDistribuicao() {

	ProdutoDistribuicaoVO vo = (ProdutoDistribuicaoVO)session.getAttribute(ProdutoDistribuicaoVO.class.getName());
	if (vo != null) {

	    MatrizDistribuicaoController matrizDistribuicaoController = new MatrizDistribuicaoController();
	    matrizDistribuicaoController.setSession(session);
	    matrizDistribuicaoController.removeItemListaDeItensDuplicadosNaSessao(vo.getIdLancamento(), vo.getIdCopia());
	    session.removeAttribute(ProdutoDistribuicaoVO.class.getName());
	}
    }

    public HttpSession getSession() {
	return session;
    }

    public void setSession(HttpSession session) {
	this.session = session;
    }


    private void carregarComboClassificacao() {

	List<TipoClassificacaoProduto> listaTipoClassificacaoProduto = tipoClassificacaoProdutoService.obterTodos();

	List<ItemDTO<Long, String>> listaTipoClassificacaoProdutoCombo = new ArrayList<ItemDTO<Long, String>>();

	for (TipoClassificacaoProduto tipoClassificacaoProduto : listaTipoClassificacaoProduto) {

            // Preenchendo a lista que irá representar o combobox de área de
            // influência na view
	    listaTipoClassificacaoProdutoCombo.add(new ItemDTO<Long, String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
	}

	result.include("listaTipoClassificacao", listaTipoClassificacaoProdutoCombo);
    }
    
    /**
     * @return the distribuicaoVendaMediaService
     */
    public DistribuicaoVendaMediaService getDistribuicaoVendaMediaService() {
        return distribuicaoVendaMediaService;
    }
    
    /**
     * @param distribuicaoVendaMediaService the distribuicaoVendaMediaService to
     *            set
     */
    public void setDistribuicaoVendaMediaService(DistribuicaoVendaMediaService distribuicaoVendaMediaService) {
        this.distribuicaoVendaMediaService = distribuicaoVendaMediaService;
    }
}
