package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoEdicoesFechadasVO implements Serializable {

	private static final long serialVersionUID = -4420202340436211254L;

	private TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> tableModel;

	private BigDecimal saldoTotal;
	
	public TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> getTableModel() {
		return tableModel;
	}
	
	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> tableModel) {
		this.tableModel = tableModel;
	}
	
	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}
	
	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}
	
}
