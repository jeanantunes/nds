package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Classe que armazena a informação de atualização da caução 
 * líquida
 * @author francisco.garcia
 *
 */
@Embeddable
public class HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date atualizacao;
    
    @Column(name = "VALOR")
    private BigDecimal valor;

    public HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida() {
    }
    
    public HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida(Date atualizacao,
            BigDecimal valor) {
        this.atualizacao = atualizacao;
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

}
