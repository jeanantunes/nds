package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoCurvaABCDistribuidor implements Serializable {

	private static final long serialVersionUID = -5667524744114236970L;

	private TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel;
	
	@Export(label = "Total de Venda de Exemplares", exhibitionOrder = 1)
	private BigDecimal totalVendaExemplares;
	
	@Export(label = "Faturamento Total", exhibitionOrder = 2)
	private BigDecimal totalFaturamento;

	public ResultadoCurvaABCDistribuidor(BigDecimal totalVendaExemplares, BigDecimal totalFaturamento) {
		this.totalVendaExemplares = totalVendaExemplares;
		this.totalFaturamento = totalFaturamento;
	}

	public TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> getTableModel() {
		return tableModel;
	}

	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel) {
		this.tableModel = tableModel;
	}

	public BigDecimal getTotalVendaExemplares() {
		return totalVendaExemplares;
	}

	public void setTotalVendaExemplares(BigDecimal totalVendaExemplares) {
		this.totalVendaExemplares = totalVendaExemplares;
	}

	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}

	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
	}

}
