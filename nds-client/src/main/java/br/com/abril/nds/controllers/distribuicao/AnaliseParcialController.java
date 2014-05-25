package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaLiberacaoEstudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.BigIntegerUtil;
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
    
    private static final String EDICOES_BASE_SESSION_ATTRIBUTE = "";

    @Path("/")
    public void index(Long id, Long faixaDe, Long faixaAte, String modoAnalise, String reparteCopiado,String dataLancamentoEdicao) {

        EstudoCotaGerado estudoCota = analiseParcialService.buscarPorId(id);
        Lancamento lancamento = lancamentoService.obterPorId(estudoCota.getEstudo().getLancamentoID());

        this.clearEdicoesBaseSession();
        
        if (modoAnalise == null) {
            result.include("tipoExibicao", "NORMAL");
            session.setAttribute("modoAnalise", "NORMAL");
        } else {
            result.include("tipoExibicao", modoAnalise);
            session.setAttribute("modoAnalise", modoAnalise);
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

    @Post
    public void historicoEdicoesBase(List<AnaliseEstudoDetalhesDTO> produtoEdicaoList) {
        List<AnaliseEstudoDetalhesDTO> list = analiseParcialService.historicoEdicoesBase(produtoEdicaoList);

        result.use(Results.json()).withoutRoot().from(list).recursive().serialize();
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
    public void carregarDetalhesCota(Integer numeroCota, String codigoProduto) {
        Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
        CotaDTO cotaDTO = analiseParcialService.buscarDetalhesCota(numeroCota, produto.getCodigoICD());

        result.use(Results.json()).withoutRoot().from(cotaDTO).recursive().serialize();
    }

    public void percentualAbrangencia(Long estudoId) {
        BigDecimal percentualAbrangencia = analiseParcialService.calcularPercentualAbrangencia(estudoId);
        result.use(Results.json()).withoutRoot().from(percentualAbrangencia).serialize();
    }

    @Path("/init")
    public void init(Long id, String sortname, String sortorder, String filterSortName, Double filterSortFrom, Double filterSortTo, String elemento,
                     Long faixaDe, Long faixaAte, List<EdicoesProdutosDTO> edicoesBase, String modoAnalise, String codigoProduto, Long numeroEdicao, String numeroCotaStr,Long estudoOrigem,String dataLancamentoEdicao) {

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
        filtroQueryDTO.setModoAnalise(session.getAttribute("modoAnalise").toString());
        filtroQueryDTO.setCodigoProduto(codigoProduto);
        filtroQueryDTO.setNumeroEdicao(numeroEdicao);
        filtroQueryDTO.setNumeroCotaStr(numeroCotaStr);
        filtroQueryDTO.setEstudoOrigem(estudoOrigem);
        filtroQueryDTO.setDataLancamentoEdicao(DateUtil.parseDataPTBR(dataLancamentoEdicao));
        

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(filtroQueryDTO);

        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
        table.setPage(1);
        table.setTotal(50);
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
    public void restaurarBaseInicial() {
    	this.clearEdicoesBaseSession();
    	this.result.nothing();
    }

    private void clearEdicoesBaseSession() {

    	this.session.removeAttribute(EDICOES_BASE_SESSION_ATTRIBUTE);
    }
    
    @Path("/cotasQueNaoEntraramNoEstudo/filtrar")
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

    @Path("/carregarEdicoesBaseEstudo")
    public void carregarEdicoesBaseEstudo(Long estudoId) {
        result.use(Results.json()).from(analiseParcialService.carregarEdicoesBaseEstudo(estudoId), "edicoesBase").recursive().serialize();
    }

    @Path("/mudarReparte")
    public void mudarReparte(Long numeroCota, Long estudoId, Long variacaoDoReparte, Long reparteDigitado) {
        analiseParcialService.atualizaReparte(estudoId, numeroCota, variacaoDoReparte, reparteDigitado);
        result.nothing();
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
    public void liberar(Long estudoId, List<CotaLiberacaoEstudo> cotas) {
    	
        analiseParcialService.liberar(estudoId, cotas);
        
        result.nothing();
    }

    private TableModel<CellModelKeyValue<AnaliseParcialDTO>> monta(List<AnaliseParcialDTO> lista) {
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        return table;
    }

    @Get("/exportar")
    public void exportar(FileType fileType, Long id) throws IOException {

        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(id);

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

        if (lista.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
        }

        FileExporter.to("Analise do Estudo", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, lista, AnaliseParcialDTO.class, this.httpResponse);

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
    public void verificacoesAntesDeLiberarEstudo (Long estudoId){
    	
    	List<EstudoCotaGerado> listEstudoCotas = analiseParcialService.obterEstudosCotaGerado(estudoId);
    	
    	EstudoGerado estudoGerado = estudoService.obterEstudo(estudoId);
    	
    	BigDecimal reparteFisicoOuPrevisto = analiseParcialService.reparteFisicoOuPrevistoLancamento(estudoId);
    	
    	for (EstudoCotaGerado estudoCota : listEstudoCotas) {
    		if(BigIntegerUtil.isMenorQueZero(estudoCota.getReparte())){
    			throw new ValidacaoException(TipoMensagem.WARNING,"Há cota(s) com reparte(s) negativo(s), por favor ajustá-la(s)!");
    		}
    	} 

    	if((reparteFisicoOuPrevisto != null)&&(estudoGerado.getReparteDistribuir().compareTo(reparteFisicoOuPrevisto.toBigInteger()) > 0)){
    		throw new ValidacaoException(TipoMensagem.WARNING,"O reparte distribuido é maior que estoque disponível!");
    	}
    	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.")).recursive().serialize();    		
    	
    }
}
