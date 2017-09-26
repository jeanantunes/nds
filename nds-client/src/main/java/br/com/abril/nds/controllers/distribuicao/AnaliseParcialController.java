package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseEstudoNormal_E_ParcialDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.AnaliseParcialExportXLSDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.ReparteFixacaoMixWrapper;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.dto.filtro.AnaliseEstudoFiltroExportPDFDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaLiberacaoEstudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuicaoVendaMediaService;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/analise/parcial")
public class AnaliseParcialController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnaliseParcialController.class);

    @Autowired
    private Validator validator;

    @Autowired
    private Result result;

    @Autowired
    private AnaliseParcialService analiseParcialService;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;

    @Autowired
    private DistribuicaoVendaMediaService distribuicaoVendaMediaService;
    
    @Autowired
    private HttpSession session;

    @Autowired
    private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private MixCotaProdutoService mixCotaProdutoService;
    
    @Autowired
    private RepartePdvService repartePdvService;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private EstudoService estudoService;
    
    @Autowired
   	private UsuarioService usuarioService;
    
    @Autowired
    private EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService;
    
    @Autowired
    private static final String EDICOES_BASE_SESSION_ATTRIBUTE = "";
    
    public static final String MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE = "mapa_analise_estudo";

    @Path("/")
    public void index(Long id, Long faixaDe, Long faixaAte, String modoAnalise, String reparteCopiado, String dataLancamentoEdicao) {

        EstudoCotaGerado estudoCota = analiseParcialService.buscarPorId(id);
        Lancamento lancamento = lancamentoService.obterPorId(estudoCota.getEstudo().getLancamentoID());
        
//        this.efetuarBloqueioAnaliseEstudo(lancamento.getProdutoEdicao().getId());
        this.bloquearEstudoPorIdProdEdicao(lancamento.getProdutoEdicao().getId());

        this.clearEdicoesBaseSession();
        
        if (modoAnalise == null) {
            result.include("tipoExibicao", "NORMAL");
            session.setAttribute("modoAnalise", "NORMAL");
        } else {
            result.include("tipoExibicao", modoAnalise);
            session.setAttribute("modoAnalise", modoAnalise);
            
            if(modoAnalise.equalsIgnoreCase("PARCIAL")){
            	List<DataLancamentoPeriodoEdicoesBasesDTO> lcmtos = analiseParcialService.obterDataLacmtoPeridoEdicoesBaseParciais(id, lancamento.getProdutoEdicao().getId());
            	
            	if(lcmtos.size() == 0){
            		List<EdicoesProdutosDTO> baseUtilizadas = analiseParcialService.carregarPeriodosAnterioresParcial(id, false);
            		
            		if(baseUtilizadas.size() == 0){
            			baseUtilizadas = analiseParcialService.carregarPeriodosAnterioresParcial(id, true);
            		}
            		
            		for (int i = 0; i < 3; i++) {
            			if(i < baseUtilizadas.size()){
            				result.include("parcial"+i, "L - "+baseUtilizadas.get(i).getDataLancamento());
            			}else{
            				result.include("parcial"+i, "L - ");
            			}
            		}
            		
            	}else{
            		for (int i = 0; i < 3; i++) {
            			if(i < lcmtos.size()){
            				result.include("parcial"+i, "L - "+lcmtos.get(i).getDataLancamento());
            			}else{
            				result.include("parcial"+i, "L - ");
            			}
            		}
            	}
            }
        }
        
        result.include("lancamentoComEstudoLiberado", (lancamento.getEstudo() != null));
        result.include("lancamento", lancamento);
        result.include("estudoCota", estudoCota);
        
        ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(id, null, null);
        
        if(resumo.getSaldo() != null){
        	estudoCota.getEstudo().setSobra(resumo.getSaldo().toBigInteger());
        } else {
        	estudoCota.getEstudo().setSobra(BigInteger.ZERO);
        }
        
    	result.include("estudo", estudoCota.getEstudo());
        result.include("faixaDe", faixaDe);
        result.include("faixaAte", faixaAte);
        result.include("reparteCopiado", reparteCopiado);
        result.include("dataLancamentoEdicao", dataLancamentoEdicao);
        result.include("classificacaoList", tipoClassificacaoProdutoService.obterTodos());

        ClassificacaoCota[] vetor = ClassificacaoCota.values();
        Arrays.sort(vetor, new Comparator<ClassificacaoCota>() {
		    @Override
		    public int compare(ClassificacaoCota o1, ClassificacaoCota o2) {
		    	return o1.getTexto().compareToIgnoreCase(o2.getTexto());
		    }
        });

        result.include("classificacaoCotaList", vetor);
        result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
        
    }
    
    @Path("/abrirAnaliseFaixa")
    public void abrirAnaliseFaixa(Long estudo, Long faixaDe, Long faixaAte) {
        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(estudo);

        AnaliseEstudoNormal_E_ParcialDTO analise = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);
        
        List<AnaliseParcialDTO> lista = analise.getAnaliseParcialDTO();
        
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
        
        table = monta(lista, table);
        table.setPage(1);
        table.setTotal(50);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
        result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
    }

    @Path("/carregarDetalhesPdv")
    public void carregarDetalhesPdv(Integer numeroCota, Long estudoId) {
        
    	List<PdvDTO> lista = analiseParcialService.carregarDetalhesPdv(numeroCota, estudoId);
        
        TableModel<CellModelKeyValue<PdvDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
        table.setPage(1);
        table.setTotal(lista.size());
        
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Post
    public void carregarDetalhesCota(Integer numeroCota, String codigoProduto, Long idClassifProdEdicao) {
        Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
        CotaDTO cotaDTO = analiseParcialService.buscarDetalhesCota(numeroCota, produto.getCodigoICD(), idClassifProdEdicao);
        
        cotaDTO.setNomeProduto(produto.getNomeComercial());
        cotaDTO.setCodigoProduto(produto.getCodigo());
        
        result.use(Results.json()).withoutRoot().from(cotaDTO).recursive().serialize();
    }

    public void percentualAbrangencia(Long estudoId) {
        BigDecimal percentualAbrangencia = analiseParcialService.calcularPercentualAbrangencia(estudoId);
        result.use(Results.json()).withoutRoot().from(percentualAbrangencia).serialize();
    }
    
    @Post
    public void reparteTotalEVendaTotalPorEdicao(List<EdicoesProdutosDTO> edicoesBase, String estudoId) {
        
    	
    	for (EdicoesProdutosDTO edicoesDTO : edicoesBase) {
    		
    		if(edicoesDTO.getProdutoEdicaoId() == null || edicoesDTO.getProdutoEdicaoId().equals("null")){
    			continue;
    		}
    		
    		ProdutoEdicao pe = produtoEdicaoService.buscarPorID(edicoesDTO.getProdutoEdicaoId()); 
    		
    		
    		DetalhesEdicoesBasesAnaliseEstudoDTO detalhes = analiseParcialService.obterReparteEVendaTotal(edicoesDTO.getCodigoProduto(), 
				edicoesDTO.getEdicao().longValue(), pe.getTipoClassificacaoProduto() != null ?pe.getTipoClassificacaoProduto().getId():null, 
				edicoesDTO.getPeriodo() != null && !edicoesDTO.getPeriodo().equals("null") ? Integer.parseInt(edicoesDTO.getPeriodo()) : null);
			
    		if(detalhes != null){
    			edicoesDTO.setReparte(detalhes.getReparte() != null ? new BigDecimal(detalhes.getReparte()) : new BigDecimal(0));
    			edicoesDTO.setVenda(detalhes.getVenda() != null ? new BigDecimal(detalhes.getVenda()) : new BigDecimal(0));
    			edicoesDTO.setDataLancamento(detalhes.getDataLancamento());
    		}
    	
    	}
    	
    	result.use(Results.json()).withoutRoot().from(edicoesBase).serialize();
    }

    @Path("/init")
    public void init(Long id, String sortname, String sortorder, int page, int rp, String filterSortName, Double filterSortFrom, Double filterSortTo, String elemento,
                     Long faixaDe, Long faixaAte, List<EdicoesProdutosDTO> edicoesBase, String modoAnalise, String codigoProduto, Long numeroEdicao, 
                     String numeroCotaStr,Long estudoOrigem,String dataLancamentoEdicao, Integer numeroParcial, String cotasFiltro, boolean isMudarBaseVisualizacao) {
    	
        AnaliseParcialQueryDTO filtroQueryDTO = new AnaliseParcialQueryDTO();
        filtroQueryDTO.setSortName(sortname);
        filtroQueryDTO.setSortOrder(sortorder);
        filtroQueryDTO.setFilterSortName(filterSortName);
        filtroQueryDTO.setFilterSortFrom(filterSortFrom);
        filtroQueryDTO.setFilterSortTo(filterSortTo);
        filtroQueryDTO.setElemento(elemento);
        filtroQueryDTO.setEdicoesBase(getEdicoesBase(edicoesBase, id, numeroParcial));
        filtroQueryDTO.setEstudoId(id);
        filtroQueryDTO.setFaixaDe(faixaDe);
        filtroQueryDTO.setFaixaAte(faixaAte);
        filtroQueryDTO.setModoAnalise(session.getAttribute("modoAnalise") != null ? session.getAttribute("modoAnalise").toString() : null);
        filtroQueryDTO.setCodigoProduto(codigoProduto);
        filtroQueryDTO.setNumeroEdicao(numeroEdicao);
        filtroQueryDTO.setNumeroCotaStr(numeroCotaStr);
        filtroQueryDTO.setEstudoOrigem(estudoOrigem);
        filtroQueryDTO.setDataLancamentoEdicao(DateUtil.parseDataPTBR(dataLancamentoEdicao));
        filtroQueryDTO.setNumeroParcial(numeroParcial);
        filtroQueryDTO.setNumeroCotasFiltro(cotasFiltro);
        filtroQueryDTO.setMudarBaseVisualizacao(isMudarBaseVisualizacao);
        
        if(filtroQueryDTO.getModoAnalise().equalsIgnoreCase("PARCIAL")){
//        	boolean isParcialComEdicaoNormal = false;
//        	
//        	if(filtroQueryDTO.getEdicoesBase() != null){
//        		for (EdicoesProdutosDTO edicoesProdutosDTO : filtroQueryDTO.getEdicoesBase()) {
//					if(!edicoesProdutosDTO.isParcial()){
//						isParcialComEdicaoNormal = true;
//					}
//				}
//        	}
        	
        	filtroQueryDTO.setParcialComEdicaoBaseNormal(true);
        }
        
        AnaliseEstudoNormal_E_ParcialDTO analise = analiseParcialService.buscaAnaliseParcialPorEstudo(filtroQueryDTO);
        
        List<AnaliseParcialDTO> lista = analise.getAnaliseParcialDTO();
        
        page = verificarPaginacaoComFiltro(page, filterSortName, filterSortFrom, filterSortTo);
        
        session.setAttribute("filtrarPor", filterSortName);
        session.setAttribute("de", filterSortFrom);
        session.setAttribute("ate", filterSortTo);
        
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
        
        paginacao.setQtdResultadosTotal(lista.size());
        
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
       
    	lista = PaginacaoUtil.paginarEmMemoria(lista, paginacao);
    	
    	table = monta(lista, table);
    	table.setPage(paginacao.getPaginaAtual());
    	table.setTotal(paginacao.getQtdResultadosTotal());
        	
        validator.onErrorUse(Results.json()).withoutRoot().from(table).recursive().serialize();
        
        AnaliseEstudoNormal_E_ParcialDTO vo = new AnaliseEstudoNormal_E_ParcialDTO();
        
        ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(id, null, null);
    	
    	vo.setTable(table);
    	vo.setTotal_qtdCotas(analise.getTotal_qtdCotas());
    	vo.setTotal_somatorioReparteSugerido(analise.getTotal_somatorioReparteSugerido());
    	vo.setTotal_somatorioUltimoReparte(analise.getTotal_somatorioUltimoReparte());
    	vo.setTotal_somatorioReparteEstudoOrigem(analise.getTotal_somatorioReparteEstudoOrigem());
    	vo.setPercentualAbrangencia(resumo.getAbrangenciaEstudo());
    	vo.setReparteTotalEdicao(analise.getReparteTotalEdicao());
    	vo.setVendaTotalEdicao(analise.getVendaTotalEdicao());
    	
    	List<EdicoesProdutosDTO> listBaseEstudo = new ArrayList<>();
    	
    	if(filtroQueryDTO.isMudarBaseVisualizacao() && filtroQueryDTO.getEdicoesBase() != null){
    		listBaseEstudo = filtroQueryDTO.getEdicoesBase();
    	}else{
    		listBaseEstudo = getEdicoesBase(edicoesBase != null ? edicoesBase : filtroQueryDTO.getEdicoesBase(), id, numeroParcial); 
    	}
    	
    	vo.setEdicoesBase(listBaseEstudo);
    	
    	if (resumo.getSaldo() != null) {
    		vo.setSaldo(resumo.getSaldo().toBigInteger());
		}
    	
    	result.use(Results.json()).from(vo).recursive().serialize();
    }

	private int verificarPaginacaoComFiltro(int page, String filterSortName, Double filterSortFrom, Double filterSortTo) {
		if(session.getAttribute("filtrarPor") != null){
        	if(!session.getAttribute("filtrarPor").equals(filterSortName)){
        		page = 1;
        	}else{
        		if((session.getAttribute("de") != null) && (!session.getAttribute("de").equals(filterSortFrom))){
        			page = 1;
        		}else{
        			if((session.getAttribute("ate") != null) && (!session.getAttribute("ate").equals(filterSortTo))){
        				page = 1;
        			}
        		}
        	}
        }
		return page;
	}
    
    @SuppressWarnings("unchecked")
	private List<EdicoesProdutosDTO> getEdicoesBase(List<EdicoesProdutosDTO> edicoesBase, Long idEstudo, Integer numeroParcial) {
    	
    	if (edicoesBase != null) {
    		
    		this.session.setAttribute(EDICOES_BASE_SESSION_ATTRIBUTE, edicoesBase);
    		
    		return edicoesBase;
    	}
    	
    	edicoesBase = (List<EdicoesProdutosDTO>) this.session.getAttribute(EDICOES_BASE_SESSION_ATTRIBUTE);
    	
    	if(edicoesBase == null){
    		
//    		if(!session.getAttribute("modoAnalise").toString().equalsIgnoreCase("PARCIAL")){
    			
			List<EdicaoBaseEstudoDTO> edicaoBaseEstudoDTOs = estudoProdutoEdicaoBaseService.obterEdicoesBase(idEstudo);
			
			if(edicaoBaseEstudoDTOs.size() > 0){
				List<EdicoesProdutosDTO> bases = new ArrayList<>();
				
				int i = 0;
				
				for (EdicaoBaseEstudoDTO edicaoBaseEstudoDTO : edicaoBaseEstudoDTOs) {
					EdicoesProdutosDTO base = new EdicoesProdutosDTO();
					
					base.setCodigoProduto(edicaoBaseEstudoDTO.getCodigoProduto());
					base.setNomeProduto(edicaoBaseEstudoDTO.getNomeProduto());
					base.setEdicao(edicaoBaseEstudoDTO.getNumeroEdicao());
					base.setParcial(edicaoBaseEstudoDTO.isParcial());
					base.setPeriodo(edicaoBaseEstudoDTO.getPeriodoParcial() != null ? edicaoBaseEstudoDTO.getPeriodoParcial().toString() : null);
					base.setProdutoEdicaoId(edicaoBaseEstudoDTO.getIdProdutoEdicao());
					base.setOrdemExibicao(i);
					
					bases.add(base);
					i++;
				}
				
				return bases;
			}
//    		}else{
//    			List<EdicoesProdutosDTO> baseUtilizadas = analiseParcialService.carregarPeriodosAnterioresParcial(idEstudo, false);
//    			
//    			if(baseUtilizadas.size() == 0){
//    				baseUtilizadas = analiseParcialService.carregarPeriodosAnterioresParcial(idEstudo, true);
//    			}
//    			
//    			return baseUtilizadas;
//    		}
    	}
    	
    	return edicoesBase;
    }
    
    @Path("/restaurarBaseInicial")
    @Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
    public void restaurarBaseInicial() {
    	this.clearEdicoesBaseSession();
    	this.result.nothing();
    }

    private void clearEdicoesBaseSession() {

    	this.session.removeAttribute(EDICOES_BASE_SESSION_ATTRIBUTE);
    }
    
    @Path("/cotasQueNaoEntraramNoEstudo/filtrar")
    @Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
    public void filtrar(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO, String sortname, String sortorder) {

        List<CotaQueNaoEntrouNoEstudoDTO> lista = new ArrayList<>();

        queryDTO.setPaginacao(new PaginacaoVO(sortname, sortorder));

        if (queryDTO.possuiNome()) {
            queryDTO.setNome(PessoaUtil.removerSufixoDeTipo(queryDTO.getNome())); 
        }

        if (queryDTO.getEstudo() != null && queryDTO.getEstudo() > 0) {
            lista = analiseParcialService.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        }

        TableModel<CellModelKeyValue<CotaQueNaoEntrouNoEstudoDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        table.setPage(1);
        table.setTotal(50);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }
    
    @Path("/mudarReparte")
    public void mudarReparte(Integer numeroCota, Long estudoId, Long fixacaoMixID, Long variacaoDoReparte, Long reparteDigitado, String legendaCota) {
        
    	analiseParcialService.atualizaReparte(estudoId, numeroCota, variacaoDoReparte, reparteDigitado);
        
        if(ClassificacaoCota.ReparteFixado.getCodigo().equalsIgnoreCase(legendaCota) || 
                ClassificacaoCota.CotaMix.getCodigo().equalsIgnoreCase(legendaCota)) {
        	
            analiseParcialService.atualizarFixacaoOuMix(new ReparteFixacaoMixWrapper(
            		fixacaoMixID, legendaCota, numeroCota, new Date(), reparteDigitado.intValue()
            	)
        	);
        }
        
        //BigDecimal percentualAbrangencia = analiseParcialService.calcularPercentualAbrangencia(estudoId);
        
        result.use(Results.json()).withoutRoot().from("").serialize();
    }

    @Post("/mudarReparteLote")
    public void mudarReparteLote(Long estudoId, List<CotaQueNaoEntrouNoEstudoDTO> cotas) {

        for (CotaQueNaoEntrouNoEstudoDTO cota : cotas) {
            analiseParcialService.atualizaReparte(estudoId, cota.getNumeroCota(),cota.getQuantidade() != null ? cota.getQuantidade().longValue():0L, cota.getQuantidade()!= null ?cota.getQuantidade().longValue():0L);
            
            if(cota.getMotivo().equalsIgnoreCase("SM")){
            	analiseParcialService.atualizaClassificacaoCota(estudoId, cota.getNumeroCota(), "MX");
            }else{
            	analiseParcialService.atualizaClassificacaoCota(estudoId, cota.getNumeroCota(), "IN");
            }
        }

        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.")).recursive().serialize();
    }
    
    @Path("/liberar")
    @Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
    public void liberar(Long estudoId, List<CotaLiberacaoEstudo> cotas) {
    	
    	ValidacaoException validacao = analiseParcialService.validarLiberacaoDeEstudo(estudoId);
    	
    	if(validacao == null || validacao.getValidacao().getTipoMensagem().equals(TipoMensagem.SUCCESS)) {
    		
    		analiseParcialService.calcularPercentualAbrangencia(estudoId);
    		
    		analiseParcialService.liberar(estudoId, cotas);
    	} else {
    		
    		if(validacao.getValidacao().getTipoMensagem().equals(TipoMensagem.WARNING)) {
    			
        		throw new ValidacaoException(validacao.getValidacao().getTipoMensagem(), validacao.getValidacao().getListaMensagens());
    		}
    	}

    	result.nothing();
    }

    private TableModel<CellModelKeyValue<AnaliseParcialDTO>> monta(List<AnaliseParcialDTO> lista, TableModel<CellModelKeyValue<AnaliseParcialDTO>> table) {
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        return table;
    }

    @Get("/exportar")
    public void exportar(FileType fileType, Long id, String tipoExibicao, Integer numeroParcial) throws IOException {

        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(id);
        queryDTO.setModoAnalise(tipoExibicao);
        queryDTO.setFile(fileType);
        
        if(tipoExibicao.equalsIgnoreCase("PARCIAL")){
        	queryDTO.setNumeroParcial(numeroParcial); 
        }

//        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);
        
        AnaliseEstudoNormal_E_ParcialDTO analise = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);
        
        AnaliseEstudoFiltroExportPDFDTO filtroAux = estudoService.obterDadosDoProdutoParaFiltroExport(id);
        
        if(fileType != null && fileType == FileType.XLS){
        	List<AnaliseParcialExportXLSDTO> listaXls = new ArrayList<>();
        	

        	listaXls = analise.getAnaliseParcialXLSDTO();
        	
        	 if (listaXls.isEmpty()) {
                 throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
             }

             FileExporter.to(
                     "Analise do Estudo", fileType).inHTTPResponse(
                             this.getNDSFileHeader(), filtroAux, listaXls, AnaliseParcialExportXLSDTO.class, this.httpResponse);
        	
        }else{
        	List<AnaliseParcialDTO> lista = new ArrayList<>();

        	lista = analise.getAnaliseParcialDTO();
        	
        	 if (lista.isEmpty()) {
                 throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
             }

             FileExporter.to(
                     "Analise do Estudo", fileType).inHTTPResponse(
                             this.getNDSFileHeader(), filtroAux, lista, AnaliseParcialDTO.class, this.httpResponse);
        }

        result.nothing();
    }

    @Post("/pesquisarProdutoEdicao")
    public void pesquisarProdutoEdicao(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao) {
        Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
        
//        List<ProdutoEdicaoVendaMediaDTO> edicoes = distribuicaoVendaMediaRepository.pesquisar(produto.getCodigoICD(), nomeProduto, edicao, idClassificacao);
        
        List<ProdutoEdicaoVendaMediaDTO> edicoes = distribuicaoVendaMediaService.pesquisar(produto.getCodigoICD(), nomeProduto, edicao, idClassificacao);
        
        TableModel<CellModelKeyValue<ProdutoEdicaoVendaMediaDTO>> table = new TableModel<>();
        
        table.setRows(CellModelKeyValue.toCellModelKeyValue(edicoes));
        table.setTotal(edicoes.size());
        table.setPage(1);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Post
    public void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap, String legenda, boolean manterFixa) {
        analiseParcialService.defineRepartePorPDV(estudoId, numeroCota, reparteMap, legenda, manterFixa);
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.")).recursive().serialize();
    }

    @Post
    public void tipoDistribuicaoCotaFiltro(TipoDistribuicaoCota tipo) {
        Integer[] cotas = analiseParcialService.buscarCotasPorTipoDistribuicao(tipo);
        result.use(Results.json()).withoutRoot().from(cotas).recursive().serialize();
    }

    @Post
    public void atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal) {
        result.use(Results.json()).withoutRoot()
                .from(analiseParcialService.atualizaReparteTotalESaldo(idEstudo, reparteTotal))
                .recursive().serialize();
    }
    
    @Post
    @Path("/verificacoesParaLiberarEstudo")
    @Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
    public void verificacoesAntesDeLiberarEstudo (Long estudoId){
    	
    	ValidacaoException validacao = analiseParcialService.validarLiberacaoDeEstudo(estudoId);
    	
    	if(validacao.getValidacao().getTipoMensagem()==TipoMensagem.WARNING){
    		throw new ValidacaoException(validacao.getValidacao().getTipoMensagem(), validacao.getValidacao().getListaMensagens());
    	}else{
    		result.use(Results.json()).from(validacao.getValidacao().getTipoMensagem(), validacao.getValidacao().getListaMensagens().get(0)).recursive().serialize();    		
    	}
    	
    }
    
	@Post 
	@Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
	public void validar(){
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO)
	public void verificarMaxMinCotaMix(final Integer numeroCota, 
	        final String codigoProduto, final Long qtdDigitado, final Long tipoClassificacaoProduto){
	    
		final Object[] ret = new Object[2];
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		 
		 if(cota!=null && !cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)){
		  
			 ret[0] = false;
		 }else {
	    
	         ret[0] = this.mixCotaProdutoService.verificarReparteMinMaxCotaProdutoMix(
	                    numeroCota, codigoProduto, qtdDigitado, tipoClassificacaoProduto);
		 }

	    
	    ret[1] = this.repartePdvService.verificarRepartePdv(numeroCota, codigoProduto);
	    
	    this.result.use(Results.json()).from(ret, "result").serialize();
	}
	
	@Post
	public void efetuarBloqueioAnaliseEstudo(Long idProdutoEdicao){
		
		bloquearEstudoPorIdProdEdicao(idProdutoEdicao);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Estudo bloqueado com sucesso."), 
				"result").recursive().serialize();
	}

	private void bloquearEstudoPorIdProdEdicao(Long idProdutoEdicao) {
		String loginUsuario = super.getUsuarioLogado().getLogin();
		
		this.bloquearAnaliseEstudo(idProdutoEdicao, this.session, loginUsuario);
	}
	
	@SuppressWarnings("unchecked")
	private void bloquearAnaliseEstudo(Long idProdutoEdicao, HttpSession session, String loginUsuario) {
		
		if (idProdutoEdicao == null) {
			 
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Estudo inválido!"));
		}
		
		// conferir se ja nao tem um estudo aberto em outra aba nesta sessao
    	String windowname_estudo=(String) session.getAttribute("WINDOWNAME_ESTUDO");
    	String windowname=(String) session.getAttribute("WINDOWNAME");
    	
      	 
    	if ( windowname_estudo != null && windowname != null && !windowname_estudo.equals(windowname)){
    		 throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Ja existe um Estudo sendo analisado em outra aba/janela"));
    	}
		
		Map<Long, String> mapaAnaliseEstudo = (Map<Long, String>) session.getServletContext().getAttribute(MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		
		if (mapaAnaliseEstudo != null) {
			
			String loginUsuarioBloqueio = mapaAnaliseEstudo.get(idProdutoEdicao);
			
			if (loginUsuarioBloqueio != null && !loginUsuarioBloqueio.equals(loginUsuario+";"+windowname)) {
				LOGGER.error("ESTE ESTUDO ja ESTA SENDO ANALISADO PELO USUARIO "+(loginUsuarioBloqueio)+
						"  BLOQUEANDO COM="+loginUsuario+";"+windowname);
				LOGGER.error("MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE=" +mapaAnaliseEstudo.toString());
				
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Este estudo já está sendo analisado pelo usuário [" 
							+ this.usuarioService.obterNomeUsuarioPorLogin(loginUsuarioBloqueio.split(";")[0]) + "]."));
			}
			
		} else {
		
			mapaAnaliseEstudo = new HashMap<Long, String>();
		}
		session.setAttribute("WINDOWNAME_ESTUDO",windowname);
		mapaAnaliseEstudo.put(idProdutoEdicao, loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		LOGGER.warn("TRAVANDO ESTUDO COM  "+loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		
		session.getServletContext().setAttribute(MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE, mapaAnaliseEstudo);
	}
	
	@SuppressWarnings("unchecked")
	public static void desbloquearAnaliseEstudo(HttpSession session, String loginUsuario) {

		Map<Long, String> mapaAnaliseEstudo = (Map<Long, String>) 
				session.getServletContext().getAttribute(MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		
		LOGGER.warn("DESBLOQUEANDO ESTUDO com "+loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		
		if (mapaAnaliseEstudo != null) {
			
			for (Map.Entry<Long, String> entry : mapaAnaliseEstudo.entrySet()) {
				
				if (entry.getValue().equals(loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"))) {
					mapaAnaliseEstudo.remove(entry.getKey());
					session.removeAttribute("WINDOWNAME_ESTUDO");
					break;
				}
			}
		}
	}
}
