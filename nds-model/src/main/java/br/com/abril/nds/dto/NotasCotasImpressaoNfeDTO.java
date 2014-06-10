package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class NotasCotasImpressaoNfeDTO extends NfeDTO {

	public NotasCotasImpressaoNfeDTO(Long numeroNota, boolean notaImpressa, Cota c, BigInteger totalExemplares, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto) {
		super();
		this.numeroNota = numeroNota;
		this.notaImpressa = notaImpressa;
		this.idCota = c.getId();
		this.nomeCota = c.getPessoa().getNome();
		this.vlrTotal = vlrTotal.setScale(2, BigDecimal.ROUND_DOWN);;
		this.vlrTotalDesconto = vlrTotal.subtract(vlrTotal.multiply(vlrTotalDesconto.divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_DOWN);;
		this.totalExemplares = totalExemplares;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	@Export(label="Núm. Nota", alignment=Alignment.LEFT)
	private Long numeroNota;
	
	@Export(label="Id Cota", alignment=Alignment.LEFT)
	private Long idCota;
	
	@Export(label="Cota", alignment=Alignment.LEFT)
	private String nomeCota;
	
	@Export(label="Total de Exemplares", alignment=Alignment.LEFT)
	private BigInteger totalExemplares;
	
	@Export(label="Valor total", alignment=Alignment.LEFT)
	private BigDecimal vlrTotal;
	
	@Export(label="Valor total com Desc.", alignment=Alignment.LEFT)
	private BigDecimal vlrTotalDesconto;
	
	private boolean notaImpressa;

	public Long getNumeroNota() {
		return numeroNota;
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

	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}

	public BigDecimal getVlrTotalDesconto() {
		return vlrTotalDesconto;
	}

	public boolean isNotaImpressa() {
		return notaImpressa;
	}

}
