package br.com.abril.nds.model.financeiro;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Embeddable
public class EmissaoBoletoAntecipado {

    @Column(name="VALOR_DEBITO", nullable = false, precision=18, scale=4)
	private BigDecimal valorDebito;
    
    @Column(name="VALOR_CREDITO", nullable = false, precision=18, scale=4)
	private BigDecimal valorCredito;
    
    @Column(name="VALOR_REPARTE", nullable = false, precision=18, scale=4)
   	private BigDecimal valorReparte;
    
    @Column(name="VALOR_ENCALHE", nullable = false, precision=18, scale=4)
   	private BigDecimal valorEncalhe;
    
    @Column(name="VALOR_CE", nullable = false, precision=18, scale=4)
	private BigDecimal valorCe;
    
    @Column(name="DATA_RECOLHIMENTO_CE_DE", nullable = true)
   	private Date dataRecolhimentoCEDe;
    
    @Column(name="DATA_RECOLHIMENTO_CE_ATE", nullable = true)
   	private Date dataRecolhimentoCEAte;
    
    @OneToOne(optional = true)
    @JoinColumn(name="BOLETO_ANTECIPADO_ID")
	private BoletoAntecipado boletoAntecipadoReimpresso;
    
    public BigDecimal getValorDebito() {
		return valorDebito;
	}

	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

	public BigDecimal getValorCe() {
		return valorCe;
	}
	
	public BigDecimal getValorReparte() {
		return valorReparte;
	}

	public void setValorReparte(BigDecimal valorReparte) {
		this.valorReparte = valorReparte;
	}

	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}

	public void setValorCe(BigDecimal valorCe) {
		this.valorCe = valorCe;
	}

	public BoletoAntecipado getBoletoAntecipadoReimpresso() {
		return boletoAntecipadoReimpresso;
	}

	public Date getDataRecolhimentoCEDe() {
		return dataRecolhimentoCEDe;
	}

	public void setDataRecolhimentoCEDe(Date dataRecolhimentoCEDe) {
		this.dataRecolhimentoCEDe = dataRecolhimentoCEDe;
	}

	public Date getDataRecolhimentoCEAte() {
		return dataRecolhimentoCEAte;
	}

	public void setDataRecolhimentoCEAte(Date dataRecolhimentoCEAte) {
		this.dataRecolhimentoCEAte = dataRecolhimentoCEAte;
	}

	public void setBoletoAntecipadoReimpresso(
			BoletoAntecipado boletoAntecipadoReimpresso) {
		this.boletoAntecipadoReimpresso = boletoAntecipadoReimpresso;
	}
}
