package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Entidade que representa a cota utilizada como
 * referência no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class HistoricoTitularidadeReferenciaCota {

    @Column(name = "NUMERO_COTA")
    private Integer numeroCota;
    
    @Column(name="PERCENTUAL")
    private BigDecimal percentual;

    /**
     * @return the numeroCota
     */
    public Integer getNumeroCota() {
        return numeroCota;
    }

    /**
     * @param numeroCota the numeroCota to set
     */
    public void setNumeroCota(Integer numeroCota) {
        this.numeroCota = numeroCota;
    }

    /**
     * @return the percentual
     */
    public BigDecimal getPercentual() {
        return percentual;
    }

    /**
     * @param percentual the percentual to set
     */
    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }
    
}
