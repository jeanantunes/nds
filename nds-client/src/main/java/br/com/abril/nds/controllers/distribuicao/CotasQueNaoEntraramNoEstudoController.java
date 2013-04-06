package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.service.CotasQueNaoEntraramNoEstudoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cotas-que-nao-entraram-no-estudo/")
public class CotasQueNaoEntraramNoEstudoController {

	private Result result;

	@Autowired
	private CotasQueNaoEntraramNoEstudoService cotasQueNaoEntraramNoEstudoService;

	public CotasQueNaoEntraramNoEstudoController(Result result) {
		this.result = result;
	}

	@Path("/filtrar")
	public void filtrar(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {

		List<CotaQueNaoEntrouNoEstudoDTO> lista = new ArrayList<>();

		if (queryDTO.getEstudo() != null && queryDTO.getEstudo() > 0) {

			lista = cotasQueNaoEntraramNoEstudoService
					.buscaCotasQuerNaoEntraramNoEstudo(queryDTO);
		}

		TableModel<CellModelKeyValue<CotaQueNaoEntrouNoEstudoDTO>> table = monta(lista);
		table.setPage(1);
		table.setTotal(50);
		result.use(Results.json()).withoutRoot().from(table).recursive()
				.serialize();
	}

	private TableModel<CellModelKeyValue<CotaQueNaoEntrouNoEstudoDTO>> monta(
			List<CotaQueNaoEntrouNoEstudoDTO> lista) {
		TableModel<CellModelKeyValue<CotaQueNaoEntrouNoEstudoDTO>> table = new TableModel<>();
		table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(
				lista)));
		return table;
	}

}
