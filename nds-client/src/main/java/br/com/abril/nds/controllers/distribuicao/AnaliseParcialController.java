package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.ReparteFixacaoMixWrapper;
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
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
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
    private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;
    
    @Autowired
    private HttpSession session;

    @Autowired
    private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private EstudoService estudoService;
    
    @Autowired
    private MixCotaProdutoService mixCotaProdutoService;
    
    @Autowired
    private RepartePdvService repartePdvService;
    
    @Autowired
    private SituacaoCotaService situacaoCotaService;
    
    @Autowired
    private CotaService cotaService;
    
    private static final String EDICOES_BASE_SESSION_ATTRIBUTE = "";

    @Path("/")
    public void index(Long id, Long faixaDe, Long faixaAte, String modoAnalise, String reparteCopiado, String dataLancamentoEdicao) {

        EstudoCotaGerado estudoCota = analiseParcialService.buscarPorId(id);
        Lancamento lancamento = lancamentoService.obterPorId(estudoCota.getEstudo().getLancamentoID());

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

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
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
				edicoesDTO.getEdicao().longValue(), pe.getTipoClassificacaoProduto().getId(), 
				!edicoesDTO.getPeriodo().equals("null") ? Integer.parseInt(edicoesDTO.getPeriodo()) : null);
			
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
                     String numeroCotaStr,Long estudoOrigem,String dataLancamentoEdicao, Integer numeroParcial) {

        AnaliseParcialQueryDTO filtroQueryDTO = new AnaliseParcialQueryDTO();
        filtroQueryDTO.setSortName(sortname);
        filtroQueryDTO.setSortOrder(sortorder);
        filtroQueryDTO.setFilterSortName(filterSortName);
        filtroQueryDTO.setFilterSortFrom(filterSortFrom);
        filtroQueryDTO.setFilterSortTo(filterSortTo);
        filtroQueryDTO.setElemento(elemento);
        filtroQueryDTO.setEdicoesBase(getEdicoesBase(edicoesBase));
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
        
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
        
        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(filtroQueryDTO);
        
        paginacao.setQtdResultadosTotal(lista.size());
        
        lista = PaginacaoUtil.paginarEmMemoria(lista, paginacao);
        
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
        table.setPage(paginacao.getPaginaAtual());
        table.setTotal(paginacao.getQtdResultadosTotal());
        
        validator.onErrorUse(Results.json()).withoutRoot().from(table).recursive().serialize();
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }
    
    @SuppressWarnings("unchecked")
	private List<EdicoesProdutosDTO> getEdicoesBase(List<EdicoesProdutosDTO> edicoesBase) {
    	
    	if (edicoesBase != null) {
    		
    		this.session.setAttribute(EDICOES_BASE_SESSION_ATTRIBUTE, edicoesBase);
    		
    		return edicoesBase;
    	}
    	
    	edicoesBase = (List<EdicoesProdutosDTO>) this.session.getAttribute(EDICOES_BASE_SESSION_ATTRIBUTE);
    	
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
        
        BigDecimal percentualAbrangencia = analiseParcialService.calcularPercentualAbrangencia(estudoId);
        
        result.use(Results.json()).withoutRoot().from(percentualAbrangencia).serialize();
    }

    @Post("/mudarReparteLote")
    public void mudarReparteLote(Long estudoId, List<CotaQueNaoEntrouNoEstudoDTO> cotas) {

        for (CotaQueNaoEntrouNoEstudoDTO cota : cotas) {
            analiseParcialService.atualizaReparte(estudoId, cota.getNumeroCota(), cota.getQuantidade().longValue(), cota.getQuantidade().longValue());
            
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
    		
    		analiseParcialService.liberar(estudoId, cotas);
    	} else {
    		
    		if(validacao.getValidacao().getTipoMensagem().equals(TipoMensagem.WARNING)) {
    			
        		throw new ValidacaoException(validacao.getValidacao().getTipoMensagem(), validacao.getValidacao().getListaMensagens());
    		}
    	}

    	result.nothing();
    }

    private TableModel<CellModelKeyValue<AnaliseParcialDTO>> monta(List<AnaliseParcialDTO> lista) {
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        return table;
    }

    @Get("/exportar")
    public void exportar(FileType fileType, Long id, String tipoExibicao, Integer numeroParcial) throws IOException {

        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(id);
        queryDTO.setModoAnalise(tipoExibicao);
        
        if(tipoExibicao.equalsIgnoreCase("PARCIAL")){
        	queryDTO.setNumeroParcial(numeroParcial); 
        }

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);
 
        if (lista.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
        }

        FileExporter.to(
                "Analise do Estudo", fileType).inHTTPResponse(
                        this.getNDSFileHeader(), null, lista, AnaliseParcialDTO.class, this.httpResponse);

        result.nothing();
    }

    @Post("/pesquisarProdutoEdicao")
    public void pesquisarProdutoEdicao(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao) {
        Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
        List<ProdutoEdicaoVendaMediaDTO> edicoes = distribuicaoVendaMediaRepository.pesquisar(produto.getCodigoICD(), nomeProduto, edicao, idClassificacao);
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
}
