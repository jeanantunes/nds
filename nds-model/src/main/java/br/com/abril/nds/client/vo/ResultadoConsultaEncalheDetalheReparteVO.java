package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoConsultaEncalheDetalheReparteVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheDetalheReparteVO>> tableModel;
	
	

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<ConsultaEncalheDetalheReparteVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<ConsultaEncalheDetalheReparteVO>> tableModel) {
		this.tableModel = tableModel;
	}
	
}
