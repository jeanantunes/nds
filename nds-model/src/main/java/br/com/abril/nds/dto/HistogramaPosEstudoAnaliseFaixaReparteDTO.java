package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
	
	private Integer reparteToOrder;
	
	public HistogramaPosEstudoAnaliseFaixaReparteDTO() {
		
	}
	
	public HistogramaPosEstudoAnaliseFaixaReparteDTO(String faixaReparte) {
		this.faixaReparte = faixaReparte;
	}

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

	public BigDecimal getReparteTotal() {
		return reparteTotal == null ? BigDecimal.ZERO : reparteTotal;
	}

	public BigDecimal getReparteMedio() {
		return reparteMedio == null ? BigDecimal.ZERO : reparteMedio;
	}

	public BigDecimal getVendaNominal() {
		return vendaNominal == null ? BigDecimal.ZERO : vendaNominal;
	}

	public BigDecimal getVendaMedia() {
		return vendaMedia == null ? BigDecimal.ZERO : vendaMedia;
	}

	public BigDecimal getVendaPercent() {
		return vendaPercent == null ? BigDecimal.ZERO : vendaPercent;
	}

	public BigDecimal getEncalheMedio() {
		return encalheMedio == null ? BigDecimal.ZERO : encalheMedio;
	}

	public BigDecimal getParticipacaoReparte() {
		return participacaoReparte == null ? BigDecimal.ZERO : participacaoReparte;
	}

	public BigInteger getQtdCotas() {
		return qtdCotas == null ? BigInteger.ZERO : qtdCotas;
	}

	public BigInteger getQtdCotaPossuemReparteMenorVenda() {
		return qtdCotaPossuemReparteMenorVenda == null ? BigInteger.ZERO : qtdCotaPossuemReparteMenorVenda;
	}

	public Integer getReparteToOrder() {
		return reparteToOrder;
	}

	public void setReparteToOrder(Integer reparteToOrder) {
		this.reparteToOrder = reparteToOrder;		
	}

	public void consolidar(HistogramaPosEstudoAnaliseFaixaReparteDTO consolidar) {
		
		this.setQtdCotas(
				this.getQtdCotas().add(consolidar.getQtdCotas())
			);
		this.setReparteTotal(
			this.getReparteTotal().add(consolidar.getReparteTotal())
		);
		
		if(this.getReparteTotal().compareTo(new BigDecimal(this.getQtdCotas())) > 0){
			this.setReparteMedio(
				this.getReparteTotal().divide(BigDecimal.valueOf(this.getQtdCotas().intValue()), 4, RoundingMode.HALF_EVEN)
			);
		}
		
		this.setVendaNominal(
			this.getVendaNominal().add(consolidar.getVendaNominal())
		);
		
		if(this.getVendaNominal().compareTo(new BigDecimal(this.qtdCotas)) > 0){
			this.setVendaMedia(
				this.getVendaNominal().divide(BigDecimal.valueOf(this.getQtdCotas().doubleValue()), 4, RoundingMode.HALF_EVEN)
			);
		}
		
		if(this.getVendaNominal().compareTo(this.reparteTotal) > 0){
			this.setVendaPercent(
				this.getVendaNominal().divide(this.getReparteTotal(), 4, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100))
			);
		}
		
		this.setEncalheMedio(
			this.getReparteMedio().subtract(this.getVendaMedia())
		);
		this.setParticipacaoReparte(
			this.getParticipacaoReparte().add(consolidar.getParticipacaoReparte())
		);
		this.setQtdCotaPossuemReparteMenorVenda(
			this.getQtdCotaPossuemReparteMenorVenda().add(consolidar.getQtdCotaPossuemReparteMenorVenda())
		);
	}
}
