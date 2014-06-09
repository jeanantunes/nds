package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class HistogramaPosEstudoAnaliseFaixaReparteDTO {

	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	private String faixaReparte;

	private BigDecimal qtdRecebida;
	
	private BigDecimal reparteTotal;
	private String reparteTotalFormatado = "0";

	private BigDecimal reparteMedio;
	private String reparteMedioFormatado = "0";

	private BigDecimal vendaNominal;
	private String vendaNominalFormatado = "0";

	private BigDecimal vendaMedia;
	private String vendaMediaFormatado = "0.0";

	private BigDecimal vendaPercent;
	private String vendaPercentFormatado = "0.0";

	private BigDecimal encalheMedio;
	private String encalheMedioFormatado = "0.0";

	private BigDecimal participacaoReparte;
	private String participacaoReparteFormatado = "0.0";

	private BigInteger qtdCotas;
	private String qtdCotasFormatado = "0";

	// Rep Menor Vda (nome da coluna no grid)
	private BigInteger qtdCotaPossuemReparteMenorVenda;
	private String qtdCotaPossuemReparteMenorVendaFormatado = "0";
	
	private String numeroCotasStr = "";

	public String getReparteTotalFormatado() {
		return reparteTotalFormatado;
	}

	public String getReparteMedioFormatado() {
		return reparteMedioFormatado;
	}

	public String getVendaNominalFormatado() {
		return vendaNominalFormatado;
	}

	public String getVendaMediaFormatado() {
		return vendaMediaFormatado;
	}

	public String getVendaPercentFormatado() {
		return vendaPercentFormatado;
	}

	public String getEncalheMedioFormatado() {
		return encalheMedioFormatado;
	}

	public String getParticipacaoReparteFormatado() {
		return participacaoReparteFormatado;
	}

	public String getQtdCotasFormatado() {
		return qtdCotasFormatado;
	}

	public String getQtdCotaPossuemReparteMenorVendaFormatado() {
		return qtdCotaPossuemReparteMenorVendaFormatado;
	}

	public BigDecimal getQtdRecebida() {
		return qtdRecebida;
	}
	
	public void setQtdRecebida(BigDecimal qtdRecebida) {
		this.qtdRecebida = qtdRecebida != null ? qtdRecebida : BigDecimal.ZERO;
	}

	public String getFaixaReparte() {
		return faixaReparte;
	}
	
	public void setFaixaReparte(String faixaReparte) {
		this.faixaReparte = faixaReparte;
	}

	public void setReparteTotal(BigDecimal reparteTotal) {
		this.reparteTotal = reparteTotal != null ? reparteTotal : BigDecimal.ZERO;
		this.reparteTotalFormatado = String.valueOf(this.reparteTotal.intValue());
	}

	public void setReparteMedio(BigDecimal reparteMedio) {
		this.reparteMedio = reparteMedio != null ? reparteMedio : BigDecimal.ZERO;
		this.reparteMedioFormatado = decimalFormat.format(this.reparteMedio.doubleValue());
	}

	public void setVendaNominal(BigDecimal vendaNominal) {
		this.vendaNominal = vendaNominal != null ? vendaNominal : BigDecimal.ZERO;
		this.vendaNominalFormatado = String.valueOf(this.vendaNominal.intValue());
	}

	public void setVendaMedia(BigDecimal vendaMedia) {
		this.vendaMedia = vendaMedia != null ? vendaMedia : BigDecimal.ZERO;
		this.vendaMediaFormatado = decimalFormat.format(this.vendaMedia.doubleValue());
	}

	public void setVendaPercent(BigDecimal vendaPercent) {
		this.vendaPercent = vendaPercent != null ? vendaPercent : BigDecimal.ZERO;
		this.vendaPercentFormatado = decimalFormat.format(this.vendaPercent.doubleValue());
	}

	public void setEncalheMedio(BigDecimal encalheMedio) {
		this.encalheMedio = encalheMedio != null ? encalheMedio : BigDecimal.ZERO;
		this.encalheMedioFormatado = decimalFormat.format(this.encalheMedio.doubleValue());
	}

	public void setParticipacaoReparte(BigDecimal participacaoReparte) {
		this.participacaoReparte = participacaoReparte != null ? participacaoReparte : BigDecimal.ZERO;
		this.participacaoReparteFormatado = decimalFormat.format(this.participacaoReparte.doubleValue());
	}

	public void setQtdCotas(BigInteger qtdCotas) {
		this.qtdCotas = qtdCotas != null ? qtdCotas : BigInteger.ZERO;
		this.qtdCotasFormatado = String.valueOf(this.qtdCotas.intValue());
	}

	public void setQtdCotaPossuemReparteMenorVenda(BigInteger qtdCotaPossuemReparteMenorVenda) {
		this.qtdCotaPossuemReparteMenorVenda = qtdCotaPossuemReparteMenorVenda != null ? qtdCotaPossuemReparteMenorVenda : BigInteger.ZERO;
		this.qtdCotaPossuemReparteMenorVendaFormatado = String.valueOf(this.qtdCotaPossuemReparteMenorVenda.intValue());
	}

	public String getNumeroCotasStr() {
		return numeroCotasStr;
	}

	public void setNumeroCotasStr(String numeroCotasStr) {
		this.numeroCotasStr = numeroCotasStr;
	}

	
	
}
