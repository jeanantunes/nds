package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Representa classe base de garantias no histórico 
 * de titularidade da cota
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_GARANTIA")
@SequenceGenerator(name="HIST_TIT_COTA_GARANTIA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class HistoricoTitularidadeCotaGarantia implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="HIST_TIT_COTA_GARANTIA_SEQ")
    @Column(name="ID")
    private Long id;
    
    @OneToOne(optional = false, cascade=CascadeType.ALL)
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    private HistoricoTitularidadeCota historicoTitularidadeCota;

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
