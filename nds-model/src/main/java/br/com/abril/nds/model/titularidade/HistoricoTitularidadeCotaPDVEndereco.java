package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Endereço do histórico de titularidade
 * do pdv da cota
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_PDV_ENDERECO")
@SequenceGenerator(name="HIST_TIT_COTA__PDV_ENDERECO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaPDVEndereco extends HistoricoTitularidadeEndereco{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA__PDV_ENDERECO_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID")
    private HistoricoTitularidadeCotaPDV historicoTitularidadeCotaPDV;

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
     * @return the historicoTitularidadeCotaPDV
     */
    public HistoricoTitularidadeCotaPDV getHistoricoTitularidadeCotaPDV() {
        return historicoTitularidadeCotaPDV;
    }

    /**
     * @param historicoTitularidadeCotaPDV the historicoTitularidadeCotaPDV to set
     */
    public void setHistoricoTitularidadeCotaPDV(
            HistoricoTitularidadeCotaPDV historicoTitularidadeCotaPDV) {
        this.historicoTitularidadeCotaPDV = historicoTitularidadeCotaPDV;
    }

}
