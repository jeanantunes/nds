package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class LancamentoPorEdicaoDTO implements Serializable  {
	
	
	private static final long serialVersionUID = -4125231140420830266L;
	
	@Export(label = "Período", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String periodo;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigDecimal reparte;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigDecimal encalhe;
	
	private BigDecimal venda;
	
	private BigDecimal vendaAcumulada;
	
	private BigDecimal percentualVenda;
	
	private String vendaFormatado;
	
	private String vendaAcumuladaFormatado;
	
	private String percentualVendaFormatado;
	
	public LancamentoPorEdicaoDTO() {
		
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento =  DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(Number reparte) {
		this.reparte = new BigDecimal(reparte != null ? reparte.longValue() : 0);
	}
	
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(Number encalhe) {
		this.encalhe = new BigDecimal(encalhe != null ? encalhe.longValue() : 0);
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(Number venda) {
		this.venda = new BigDecimal(venda != null ? venda.longValue() : 0);
		this.vendaFormatado = CurrencyUtil.formatarValor(this.venda);
	}
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 6)
	public String getVendaFormatado() {
		return vendaFormatado;
	}
	
	public BigDecimal getVendaAcumulada() {
		return vendaAcumulada;
	}

	public void setVendaAcumulada(Number vendaAcumulada) {
		this.vendaAcumulada = new BigDecimal(vendaAcumulada != null ? vendaAcumulada.longValue() : 0);
		this.vendaAcumuladaFormatado = CurrencyUtil.formatarValor(this.vendaAcumulada);
	}
	
	@Export(label = "Venda Acumulada", alignment=Alignment.CENTER, exhibitionOrder = 7)
	public String getVendaAcumuladaFormatado() {
		return vendaAcumuladaFormatado;
	}

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(Number percentualVenda) {
		this.percentualVenda = new BigDecimal(percentualVenda != null ? percentualVenda.longValue() : 0);
		this.percentualVendaFormatado = CurrencyUtil.formatarValor(this.percentualVenda);
	}
	
	@Export(label = "% Venda", alignment=Alignment.CENTER, exhibitionOrder = 8)
	public String getPercentualVendaFormatado() {
		return percentualVendaFormatado;
	}

}
