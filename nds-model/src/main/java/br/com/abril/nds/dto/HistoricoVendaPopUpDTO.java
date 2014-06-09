package br.com.abril.nds.dto;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class HistoricoVendaPopUpDTO {

	private TableModel<CellModelKeyValue<PdvDTO>> tableModel;
	private HistoricoVendaPopUpCotaDto cotaDto;
	
	public TableModel<CellModelKeyValue<PdvDTO>> getTableModel() {
		return tableModel;
	}

	public void setTableModel(
			TableModel<CellModelKeyValue<PdvDTO>> tableModel) {
		this.tableModel = tableModel;
	}

	public HistoricoVendaPopUpCotaDto getCotaDto() {
		return cotaDto;
	}

	public void setCotaDto(HistoricoVendaPopUpCotaDto cotaDto) {
		this.cotaDto = cotaDto;
	}

}
