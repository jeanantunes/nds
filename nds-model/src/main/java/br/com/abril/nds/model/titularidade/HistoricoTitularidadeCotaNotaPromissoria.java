package br.com.abril.nds.model.titularidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Representa a garantia do tipo "NOTA PROMISSORIA" no histórico de
 * titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("NOTA_PROMISSORIA")
public class HistoricoTitularidadeCotaNotaPromissoria extends HistoricoTitularidadeCotaGarantia {
    
    @Temporal(TemporalType.DATE)
    @Column(name="NOTA_PROMISSORIA_VENCIMENTO", nullable=false)
    private Date vencimento;
    
    @Column(name="NOTA_PROMISSORIA_VALOR", nullable=false)
    private Double valor;
    
    @Column(name="NOTA_PROMISSORIA_VALOR_EXTENSO", nullable=false)
    private String valorExtenso;

    /**
     * @return the vencimento
     */
    public Date getVencimento() {
        return vencimento;
    }

    /**
     * @param vencimento the vencimento to set
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the valorExtenso
     */
    public String getValorExtenso() {
        return valorExtenso;
    }

    /**
     * @param valorExtenso the valorExtenso to set
     */
    public void setValorExtenso(String valorExtenso) {
        this.valorExtenso = valorExtenso;
    }

}
