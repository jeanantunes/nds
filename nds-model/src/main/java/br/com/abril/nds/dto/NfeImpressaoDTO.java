package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NfeImpressaoDTO extends NfeDTO {

	
	public NfeImpressaoDTO() {
		super();
	}
	
	public NfeImpressaoDTO(NotaFiscal notafiscal, Cota cota) {
		super();
		this.idCota = cota.getId();
		this.nomeCota = cota.getPessoa().getNome();
		this.vlrTotal = notafiscal.getInformacaoValoresTotais().getValorNotaFiscal();
		this.vlrTotalDesconto = notafiscal.getInformacaoValoresTotais().getValorDesconto();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private Long idCota;
	
	private String nomeCota;
	
	private BigDecimal vlrTotal;
	
	private BigDecimal vlrTotalDesconto;

	public Long getIdCota() {
		return idCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}

	public BigDecimal getVlrTotalDesconto() {
		return vlrTotalDesconto;
	}
	
}
