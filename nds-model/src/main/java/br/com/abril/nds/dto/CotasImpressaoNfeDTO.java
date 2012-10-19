package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;


public class CotasImpressaoNfeDTO extends NfeDTO {

	public CotasImpressaoNfeDTO() {
		super();
	}
	
	public CotasImpressaoNfeDTO(Cota c, NotaFiscal nf, BigDecimal vlrTotal, BigDecimal vlrTotalDesconto) {
		super();
		this.idNotaFiscal = nf.getId();
		this.dataEmissao = nf.getIdentificacao().getDataEmissao();
		this.tipoEmissaoNF = nf.getIdentificacao().getTipoEmissao();
		this.idCota = c.getId();
		this.nomeCota = c.getPessoa().getNome();
		this.vlrTotal = vlrTotal;
		this.vlrTotalDesconto = vlrTotalDesconto;
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private NotaFiscal nf;
	
	private Date dataEmissao;
	
	private TipoEmissao tipoEmissaoNF;
	
	private Long idNotaFiscal;
	
	private BigDecimal vlrTotal;
	
	private BigDecimal vlrTotalDesconto;
	
	private Long idCota;
	
	private String nomeCota;
	
	private BigInteger totalExemplares;
	
	private boolean notaImpressa;

	public NotaFiscal getNf() {
		return nf;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public TipoEmissao getTipoEmissaoNF() {
		return tipoEmissaoNF;
	}

	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}

	public Long getIdNotaFiscal() {
		return idNotaFiscal;
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
