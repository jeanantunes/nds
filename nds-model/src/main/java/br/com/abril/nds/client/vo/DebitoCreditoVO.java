package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class DebitoCreditoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModel> tableModel;
	
	@Export(label = "", alignWithHeader = "Total R$",columnType=ColumnType.DECIMAL)
	private String valorTotal;

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModel> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModel> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
}
