package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoEdicoesFechadasVO implements Serializable {

	private static final long serialVersionUID = -4420202340436211254L;

	private TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> tableModel;

	@Export(label = "Total", alignWithHeader = "Saldo", alignment = Alignment.CENTER)
	private BigInteger saldoTotal;
	
	public TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> getTableModel() {
		return tableModel;
	}
	
	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> tableModel) {
		this.tableModel = tableModel;
	}

	public BigInteger getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigInteger saldoTotal) {
		this.saldoTotal = saldoTotal;
	}
	
}
