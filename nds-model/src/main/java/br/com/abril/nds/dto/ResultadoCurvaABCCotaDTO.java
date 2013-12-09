package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoCurvaABCCotaDTO implements Serializable {

	private static final long serialVersionUID = -1269456885268785522L;

	private TableModel<CellModelKeyValue<RegistroCurvaABCCotaDTO>> tableModel;
	
	private BigInteger totalVendaExemplares;
	
	private BigDecimal totalFaturamento;

	@Export(label = "Total", alignWithHeader="Venda de Exemplares")
	private String totalVendaExemplaresFormatado;
	
	@Export(label = "", alignWithHeader="Faturamento R$")
	private String totalFaturamentoFormatado;

	public TableModel<CellModelKeyValue<RegistroCurvaABCCotaDTO>> getTableModel() {
		return tableModel;
	}

	public void setTableModel(
			TableModel<CellModelKeyValue<RegistroCurvaABCCotaDTO>> tableModel) {
		this.tableModel = tableModel;
	}

	public BigInteger getTotalVendaExemplares() {
		return totalVendaExemplares;
	}

	public void setTotalVendaExemplares(BigInteger totalVendaExemplares) {
		this.totalVendaExemplares = totalVendaExemplares;
		
		totalVendaExemplaresFormatado = totalVendaExemplares == null ? "0" : totalVendaExemplares.toString();
	}

	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}

	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
		
		totalFaturamentoFormatado = CurrencyUtil.formatarValor((totalFaturamento== null)?BigDecimal.ZERO: totalFaturamento);
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