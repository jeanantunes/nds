package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoVendaEncalheVO {

	private TableModel<CellModelKeyValue<VendaEncalheVO>> tableModel;
	
	@Export(label = "Total Geral", alignWithHeader = "Total R$", alignment = Alignment.RIGHT)
	private String totalGeral;
	
	public ResultadoVendaEncalheVO() {}

	public ResultadoVendaEncalheVO(	TableModel<CellModelKeyValue<VendaEncalheVO>> tableModel,
									String totalGeral) {
		this.tableModel = tableModel;
		this.totalGeral = totalGeral;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<VendaEncalheVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<VendaEncalheVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the totalGeral
	 */
	public String getTotalGeral() {
		return totalGeral;
	}

	/**
	 * @param totalGeral the totalGeral to set
	 */
	public void setTotalGeral(String totalGeral) {
		this.totalGeral = totalGeral;
	}

}
