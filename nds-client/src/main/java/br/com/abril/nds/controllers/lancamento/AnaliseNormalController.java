package br.com.abril.nds.controllers.lancamento;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseNormalDTO;
import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.AnaliseNormalProdutoEdicaoDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.service.AnaliseNormalService;
import br.com.abril.nds.service.LancamentoService;
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
@Path("/lancamento/analise/normal")
public class AnaliseNormalController extends BaseController{
	
	private Result result;
	
	@Autowired
	private AnaliseNormalService analiseNormalService;
	
	@Autowired
    private LancamentoService lancamentoService;

	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private HttpSession session;
	
	public AnaliseNormalController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	public void index(Long id){
		EstudoGerado estudo = analiseNormalService.buscarPorId(id);
		Lancamento lancamento = lancamentoService.obterPorId(estudo.getLancamentoID());
		result.include("lancamentoComEstudoLiberado", (lancamento.getEstudo() != null));
		result.include("estudo", estudo);
		result.forwardTo("/WEB-INF/jsp/lancamento/analiseNormal.jsp");
	}

	@Path("/init")
	public void init(Long id, String[] sortname, String[] sortorder, 
			String filterSortName, Double filterSortFrom, Double filterSortTo,String edicoes,
			String elemento){
		int[] arrayEdicoes = null;
		if(edicoes != null){
			arrayEdicoes = new int[6];
			String[] e = edicoes.split(",");
			for (int i = 0; i < e.length && i < arrayEdicoes.length; i++) {
				arrayEdicoes[i]=Integer.parseInt(e[i].trim());
			}
		}
		AnaliseNormalQueryDTO queryDTO = new AnaliseNormalQueryDTO();
		queryDTO.sortGrid(sortname[sortname.length-1], sortorder[sortorder.length-1]);
		queryDTO.estudoId(id);
		queryDTO.filterSort(filterSortName, filterSortFrom, filterSortTo);
		queryDTO.edicoes(arrayEdicoes);
		queryDTO.setElemento(elemento);
		List<AnaliseNormalDTO> lista = analiseNormalService.buscaAnaliseNormalPorEstudo(queryDTO);
		TableModel<CellModelKeyValue<AnaliseNormalDTO>> table = monta(lista);
		result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
	}
	
	@Path("/produtosParaGrid")
	public void produtosParaGrid(Long id){
		TableModel<CellModelKeyValue<AnaliseNormalProdutoEdicaoDTO>> monta = monta(analiseNormalService.buscaProdutoParaGrid(id));
		result.use(Results.json()).withoutRoot().from(monta).recursive().serialize();
	}
	
	@Path("/mudarReparte")
	public void mudarReparte(Long numeroCota, Long estudoId, Long reparte){
		analiseNormalService.atualizaReparte(estudoId, numeroCota, reparte);
		result.nothing();
	}
	
	private <T> TableModel<CellModelKeyValue<T>> monta(List<T> lista) {
		TableModel<CellModelKeyValue<T>> table = new TableModel<>();
		table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
		table.setPage(1);
		table.setTotal(lista.size());
		return table;
	}
	
	@Path("/liberar")
	public void liberar(Long id) {
		
		analiseNormalService.liberar(id);
		
		result.nothing();
	}
	
	@Get("/exportar")
	public void exportar(FileType fileType, Long id) throws IOException {
		
		AnaliseNormalQueryDTO queryDTO = new AnaliseNormalQueryDTO();
		queryDTO.estudoId(id);

		List<AnaliseNormalDTO> lista = analiseNormalService.buscaAnaliseNormalPorEstudo(queryDTO);

		if (lista.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, lista, AnaliseNormalDTO.class, this.httpResponse);

		result.nothing();
	}
	
}
