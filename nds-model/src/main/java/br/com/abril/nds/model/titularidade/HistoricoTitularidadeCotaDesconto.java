package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidade que representa a hierarquia de desconto da cota no histórico de
 * titularidade
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_DESCONTO")
@SequenceGenerator(name="HIST_TIT_COTA_DESCONTO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class HistoricoTitularidadeCotaDesconto implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_DESCONTO_SEQ")
    @Column(name = "ID")
    protected Long id;
    
    /**
     * Data última atualização
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATUALIZACAO", nullable = false)
    protected Date atualizacao;
    
    /**
     * Porcentagem do desconto
     */
    @Column(name = "DESCONTO", nullable = false)
    protected BigDecimal desconto;
    
    @ManyToOne
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    protected HistoricoTitularidadeCota historicoTitularidadeCota;
    

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return the desconto
     */
    public BigDecimal getDesconto() {
        return desconto;
    }

    /**
     * @param desconto the desconto to set
     */
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    /**
     * @return the historicoTitularidadeCota
     */
    public HistoricoTitularidadeCota getHistoricoTitularidadeCota() {
        return historicoTitularidadeCota;
    }

    /**
     * @param historicoTitularidadeCota the historicoTitularidadeCota to set
     */
    public void setHistoricoTitularidadeCota(
            HistoricoTitularidadeCota historicoTitularidadeCota) {
        this.historicoTitularidadeCota = historicoTitularidadeCota;
    }
}
