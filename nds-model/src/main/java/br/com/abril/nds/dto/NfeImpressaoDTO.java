package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.Cota;


public class NfeImpressaoDTO extends NfeDTO {

	public NfeImpressaoDTO() {
		super();
	}
	
	public NfeImpressaoDTO(Cota c, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto) {
		super();
		
		this.idCota = c.getId();
		this.nomeCota = c.getPessoa().getNome();
		this.vlrTotal = vlrTotal;
		this.vlrTotalDesconto = vlrTotalDesconto;
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private Long idNotaFiscal;
	
	private BigDecimal vlrTotal;
	
	private BigDecimal vlrTotalDesconto;
	
	private Long idCota;
	
	private String nomeCota;

	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}

	public BigDecimal getVlrTotal() {
		return vlrTotal;
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
	
}
