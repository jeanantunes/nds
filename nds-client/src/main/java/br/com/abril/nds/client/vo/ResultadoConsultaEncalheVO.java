package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoConsultaEncalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel;
	
	/**
	 * Obtém tableModel
	 *
	 * @return TableModel<CellModelKeyValue<ConsultaEncalheVO>>
	 */
	public TableModel<CellModelKeyValue<ConsultaEncalheVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * Atribuí tableModel
	 * @param tableModel 
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel) {
		this.tableModel = tableModel;
	}

}
