package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
	private BigInteger reparte;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger encalhe;
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger venda;
	
	@Export(label = "Venda Acumulada", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private BigInteger vendaAcumulada;
	
	private BigDecimal percentualVenda;
	
	@Export(label = "% Venda", alignment=Alignment.CENTER, exhibitionOrder = 8)
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

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	
	public BigInteger getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	
	public BigInteger getVendaAcumulada() {
		return vendaAcumulada;
	}

	public void setVendaAcumulada(BigInteger vendaAcumulada) {
		this.vendaAcumulada = vendaAcumulada;
	}

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(BigDecimal percentualVenda) {
		this.percentualVenda = percentualVenda;
		this.percentualVendaFormatado = CurrencyUtil.formatarValor(this.percentualVenda);
	}
	
	public String getPercentualVendaFormatado() {
		return percentualVendaFormatado;
	}

}
