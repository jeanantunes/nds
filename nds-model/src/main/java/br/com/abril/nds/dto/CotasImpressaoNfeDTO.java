package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;


public class CotasImpressaoNfeDTO extends NfeDTO {

	public CotasImpressaoNfeDTO(Cota c, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto) {
		super();
		this.idCota = c.getId();
		this.nomeCota = c.getPessoa().getNome();
		this.vlrTotal = vlrTotal;
		this.vlrTotalDesconto = vlrTotal.subtract(vlrTotalDesconto);
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private BigDecimal vlrTotal;
	
	private BigDecimal vlrTotalDesconto;
	
	private Long idCota;
	
	private String nomeCota;
	
	private BigInteger totalExemplares;
	
	private boolean notaImpressa;

	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}

	public void setVlrTotal(BigDecimal vlrTotal) {
		this.vlrTotal = vlrTotal;
	}

	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}
	
	public void setVlrTotalDesconto(BigDecimal vlrTotalDesconto) {
		this.vlrTotalDesconto = vlrTotalDesconto;
	}

	public BigDecimal getVlrTotalDesconto() {
		return vlrTotalDesconto;
	}

	public Long getIdCota() {
		return idCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public BigInteger getTotalExemplares() {
		return totalExemplares;
	}

	public void setTotalExemplares(BigInteger totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

}
