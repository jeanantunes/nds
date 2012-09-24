package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.TipoGarantia;

/**
 * Representa a garantia do tipo "OUTROS" no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("OUTROS")
public class HistoricoTitularidadeCotaOutros extends HistoricoTitularidadeCotaGarantia {

    private static final long serialVersionUID = 1L;

    @Column(name="OUTROS_DESCRICAO")
    private String descricao;
    
    @Column(name="OUTROS_VALOR")
    private BigDecimal valor;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="OUTROS_DATA_VALIDADE")
    private Date validade;
    
    public HistoricoTitularidadeCotaOutros() {
        this.tipoGarantia = TipoGarantia.OUTROS;
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
     * @return the validade
     */
    public Date getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Date validade) {
        this.validade = validade;
    }
   
}
