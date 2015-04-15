package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NotasCotasImpressaoNfeDTO extends NfeDTO {

	public NotasCotasImpressaoNfeDTO(){
		super();
	}
	
	public NotasCotasImpressaoNfeDTO(Long idNota, Long numeroNota, Boolean notaImpressa, Cota c, BigInteger totalExemplares, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto) {
		super();
		this.idNota = idNota;
		this.numeroNota = numeroNota;
		this.notaImpressa = notaImpressa;
		this.idCota = c.getId();
		this.nomeCota = c.getPessoa().getNome();
		this.numeroCota = c.getNumeroCota();
		this.vlrTotal = vlrTotal.setScale(2, BigDecimal.ROUND_DOWN);;
		this.vlrTotalDesconto = vlrTotal.subtract(vlrTotal.multiply(vlrTotalDesconto.divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_DOWN);;
		this.totalExemplares = totalExemplares;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private Long idNota;
	
	@Export(label="Núm. Nota", alignment=Alignment.LEFT)
	private Long numeroNota;
	
	@Export(label="Id Cota", alignment=Alignment.LEFT)
	private Long idCota;
	
	@Export(label="Id Cota", alignment=Alignment.LEFT)
	private Integer numeroCota;
	
	@Export(label="Cota", alignment=Alignment.LEFT)
	private String nomeCota;
	
	@Export(label="Total de Exemplares", alignment=Alignment.LEFT)
	private BigInteger totalExemplares;
	
	@Export(label="Valor total", alignment=Alignment.LEFT)
	private BigDecimal vlrTotal;
	
	@Export(label="Valor total com Desc.", alignment=Alignment.LEFT)
	private BigDecimal vlrTotalDesconto;
	
	private boolean notaImpressa;
	
	public Long getIdNota() {
		return idNota;
	}

	public void setIdNota(Long idNota) {
		this.idNota = idNota;
	}

	public Long getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Long getIdCota() {
		return idCota;
	}
	
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigInteger getTotalExemplares() {
		return totalExemplares;
	}

	public void setTotalExemplares(BigInteger totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}

	public void setVlrTotal(BigDecimal vlrTotal) {
		this.vlrTotal = vlrTotal;
	}

	public BigDecimal getVlrTotalDesconto() {
		return vlrTotalDesconto;
	}

	public void setVlrTotalDesconto(BigDecimal vlrTotalDesconto) {
		this.vlrTotalDesconto = vlrTotalDesconto;
	}

	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}

}