package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Representa a garantia do tipo "CAUCAO LIQUIDA" no histórico de
 * titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("CAUCAO_LIQUIDA")
public class HistoricoTitularidadeCotaCaucaoLiquida extends HistoricoTitularidadeCotaGarantia {
    
    @Column(name="CAUCAO_LIQUIDA_VALOR")
    private BigDecimal valor;
    
    @Column(name="CAUCAO_LIQUIDA_DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date atualizacao;
    
    @Column(name="CAUCAO_LIQUIDA_INDICE_REAJUSTE")
    private BigDecimal indiceReajuste;

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the atualizacao
     */
    public Date getAtualizacao() {
        return atualizacao;
    }

    /**
     * @param atualizacao the atualizacao to set
     */
    public void setAtualizacao(Date atualizacao) {
        this.atualizacao = atualizacao;
    }

    /**
     * @return the indiceReajuste
     */
    public BigDecimal getIndiceReajuste() {
        return indiceReajuste;
    }

    /**
     * @param indiceReajuste the indiceReajuste to set
     */
    public void setIndiceReajuste(BigDecimal indiceReajuste) {
        this.indiceReajuste = indiceReajuste;
    }
    
}
