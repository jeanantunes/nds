package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoCurvaABCEditor implements Serializable {

	private static final long serialVersionUID = -220679988647432376L;

	private TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>> tableModel;
	
	private BigInteger totalVendaExemplares;
	
	private BigDecimal totalFaturamento;

	@Export(label = "Total", alignWithHeader="Venda de Exemplares", columnType = ColumnType.NUMBER)
	private String totalVendaExemplaresFormatado;

	@Export(label = "", alignWithHeader="Faturamento da Capa", columnType = ColumnType.DECIMAL)
	private String totalFaturamentoFormatado;

	public TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>> getTableModel() {
		return tableModel;
	}

	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>> tableModel) {
		this.tableModel = tableModel;
	}

	public BigInteger getTotalVendaExemplares() {
		return totalVendaExemplares;
	}

	public void setTotalVendaExemplares(BigInteger totalVendaExemplares) {
		this.totalVendaExemplares = totalVendaExemplares;
		
		this.totalVendaExemplaresFormatado = totalVendaExemplares == null ? "0" : totalVendaExemplares.toString();
	}

	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}

	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
		
		this.totalFaturamentoFormatado = CurrencyUtil.formatarValor(totalFaturamento);
	}

	public String getTotalVendaExemplaresFormatado() {
		return totalVendaExemplaresFormatado;
	}

	public void setTotalVendaExemplaresFormatado(
			String totalVendaExemplaresFormatado) {
		this.totalVendaExemplaresFormatado = totalVendaExemplaresFormatado;
	}

	public String getTotalFaturamentoFormatado() {
		return totalFaturamentoFormatado;
	}

	public void setTotalFaturamentoFormatado(String totalFaturamentoFormatado) {
		this.totalFaturamentoFormatado = totalFaturamentoFormatado;
	}

}
