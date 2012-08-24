package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Representa garantia do fiador no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaFiadorGarantia implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Valor da garantia
     */
    @Column(name = "GARANTIA_VALOR")
    private BigDecimal valor;
    
    /**
     * Descrição da garantia
     */
    @Column(name = "GARANTIA_DESCRICAO")
    private String descricao;

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
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
}
