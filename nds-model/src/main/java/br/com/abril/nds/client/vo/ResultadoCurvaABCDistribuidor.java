package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoCurvaABCDistribuidor implements Serializable {

	private static final long serialVersionUID = -5667524744114236970L;

	private TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel;
	
	private BigInteger totalVendaExemplares;
	
	private BigDecimal totalFaturamento;

	@Export(label = "Total", alignWithHeader="Venda de Exemplares")
	private String totalVendaExemplaresFormatado;
	
	@Export(label = "", alignWithHeader="Faturamento da Capa")
	private String totalFaturamentoFormatado;
	
	public ResultadoCurvaABCDistribuidor(BigInteger totalVendaExemplares, BigDecimal totalFaturamento) {
		this.totalVendaExemplares = totalVendaExemplares;
		this.totalFaturamento = totalFaturamento;
		this.formatarCampos();
	}

	public TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> getTableModel() {
		return tableModel;
	}

	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel) {
		this.tableModel = tableModel;
	}

	public BigInteger getTotalVendaExemplares() {
		return totalVendaExemplares;
	}

	public void setTotalVendaExemplares(BigInteger totalVendaExemplares) {
		this.totalVendaExemplares = totalVendaExemplares;
	}

	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}

	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
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

	private void formatarCampos() {
		this.totalVendaExemplaresFormatado = CurrencyUtil.formatarValor(totalVendaExemplares);
		this.totalFaturamentoFormatado = CurrencyUtil.formatarValor(totalFaturamento);
	}

}