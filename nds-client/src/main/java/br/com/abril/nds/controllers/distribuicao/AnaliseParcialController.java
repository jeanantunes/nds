package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.service.AnaliseParcialService;
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

	private Result result;

	@Autowired
	private AnaliseParcialService analiseParcialService;

	@Autowired
	private HttpServletResponse httpResponse;

	public AnaliseParcialController(Result result) {
		this.result = result;
	}

	@Path("/")
	public void index(Long id) {

		EstudoCota estudo = analiseParcialService.buscarPorId(id);
		result.include("estudoCota", estudo);
		result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
	}

	@Path("/init")
	public void init(Long id, String sortname, String sortorder,
			String filterSortName, Double filterSortFrom, Double filterSortTo,
			String elemento) {

		AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
		queryDTO.setEstudoId(id);
		queryDTO.setSortName(sortname);
		queryDTO.setSortOrder(sortorder);
		queryDTO.setFilterSortName(filterSortName);
		queryDTO.setFilterSortFrom(filterSortFrom);
		queryDTO.setFilterSortTo(filterSortTo);
		queryDTO.setElemento(elemento);

		List<AnaliseParcialDTO> lista = analiseParcialService
				.buscaAnaliseParcialPorEstudo(queryDTO);

		TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
		table.setPage(1);
		table.setTotal(50);
		result.use(Results.json()).withoutRoot().from(table).recursive()
				.serialize();
	}

	@Path("/mudarReparte")
	public void mudarReparte(Long numeroCota, Long estudoId, Double reparte) {
		analiseParcialService.atualizaReparte(estudoId, numeroCota, reparte);
		result.nothing();
	}

	@Path("/liberar")
	public void liberar(Long id) {
		analiseParcialService.liberar(id);
		result.nothing();
	}

	private TableModel<CellModelKeyValue<AnaliseParcialDTO>> monta(
			List<AnaliseParcialDTO> lista) {
		TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
		table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(
				lista)));
		return table;
	}

	@Get("/exportar")
	public void exportar(FileType fileType, Long id) throws IOException {
		
		AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
		queryDTO.setEstudoId(id);

		List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

		if (lista.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A pesquisa realizada não obteve resultado.");
		}

		FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null, lista,
				AnaliseParcialDTO.class, this.httpResponse);

		result.nothing();
	}

}
