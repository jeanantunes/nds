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

	@Export(label = "Total de Venda de Exemplares", exhibitionOrder = 1)
	private String totalVendaExemplaresFormatado;
	
	@Export(label = "Faturamento Total", exhibitionOrder = 2)
	private String totalFaturamentoFormatado;
	
	public ResultadoCurvaABCCotaDTO(BigInteger totalVendaExemplares, BigDecimal totalFaturamento) {
		this.totalVendaExemplares = totalVendaExemplares;
		this.totalFaturamento = totalFaturamento;
		formatarCampos();
	}

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
		totalFaturamentoFormatado = CurrencyUtil.formatarValor((totalFaturamento== null)?BigDecimal.ZERO: totalFaturamento); 
		totalVendaExemplaresFormatado = CurrencyUtil.formatarValorTruncado((totalVendaExemplares == null)?BigDecimal.ZERO:totalVendaExemplares);
	}
	
}
