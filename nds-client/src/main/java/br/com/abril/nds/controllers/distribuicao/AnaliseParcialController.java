package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/analise/parcial")
public class AnaliseParcialController extends BaseController {

    @Autowired
    private Result result;

    @Autowired
    private AnaliseParcialService analiseParcialService;

    @Autowired
    private HttpServletResponse httpResponse;
    
    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;

    public AnaliseParcialController(Result result) {
	this.result = result;
    }

    @Path("/")
    public void index(Long id, Long faixaDe, Long faixaAte, String modoAnalise) {

	EstudoCota estudo = analiseParcialService.buscarPorId(id);
	if (modoAnalise == null) {
	    result.include("tipoExibicao", "NORMAL");
	} else {
	    result.include("tipoExibicao", modoAnalise);
	}
	result.include("estudoCota", estudo);
	result.include("faixaDe", faixaDe);
	result.include("faixaAte", faixaAte);
	result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
    }

    @Path("/detalhes")
    public void detalhes(Long id) {
	ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(id);
	List<AnaliseEstudoDetalhesDTO> lista = analiseParcialService.buscarDetalhesAnalise(produtoEdicao);
	
	TableModel<CellModelKeyValue<AnaliseEstudoDetalhesDTO>> table = new TableModel<>();
	table.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
	table.setPage(1);
	result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
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

    @Path("/carregarDetalhesCota")
    public void carregarDetalhesCota(Long numeroCota) {
	List<PdvDTO> lista = analiseParcialService.carregarDetalhesCota(numeroCota);
	
	TableModel<CellModelKeyValue<PdvDTO>> table = new TableModel<>();
	table.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
	table.setPage(1);
	result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }
    
    @Path("/init")
    public void init(Long id, String sortname, String sortorder, String filterSortName, Double filterSortFrom, Double filterSortTo, String elemento,
	    Long faixaDe, Long faixaAte, List<EdicoesProdutosDTO> edicoesBase, String modoAnalise, String codigoProduto, Long numeroEdicao) {

	AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
	queryDTO.setSortName(sortname);
	queryDTO.setSortOrder(sortorder);
	queryDTO.setFilterSortName(filterSortName);
	queryDTO.setFilterSortFrom(filterSortFrom);
	queryDTO.setFilterSortTo(filterSortTo);
	queryDTO.setElemento(elemento);
	queryDTO.setEdicoesBase(edicoesBase);
	queryDTO.setEstudoId(id);
	queryDTO.setFaixaDe(faixaDe);
	queryDTO.setFaixaAte(faixaAte);
	queryDTO.setModoAnalise(modoAnalise);
	queryDTO.setCodigoProduto(codigoProduto);
	queryDTO.setNumeroEdicao(numeroEdicao);
	
	List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

	TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
	table.setPage(1);
	table.setTotal(50);
	result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Path("/cotasQueNaoEntraramNoEstudo/filtrar")
    public void filtrar(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {

	List<CotaQueNaoEntrouNoEstudoDTO> lista = new ArrayList<>();

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
    public void mudarReparte(Long numeroCota, Long estudoId, Long variacaoDoReparte) {
	analiseParcialService.atualizaReparte(estudoId, numeroCota, variacaoDoReparte);
	result.nothing();
    }

    @Path("/liberar")
    public void liberar(Long id) {
	analiseParcialService.liberar(id);
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

	FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, lista, AnaliseParcialDTO.class,
		this.httpResponse);

	result.nothing();
    }

}
