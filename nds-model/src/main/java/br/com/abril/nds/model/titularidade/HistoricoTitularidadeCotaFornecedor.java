package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Representa o fornecedor no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_FORNECEDOR")
@SequenceGenerator(name="HIST_TIT_COTA_FORN_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaFornecedor implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_FORN_SEQ")
    @Column(name = "ID")
    private Long id;
    
    /**
     * Identificar do fornecedor origem do registro
     */
    @Column(name = "ID_ORIGEM")
    private Long idOrigem;
    
    /**
     * Pessoa Jurídica que corresponde ao fornecedor
     */
    @Embedded
    private HistoricoTitularidadeCotaPessoaJuridica pessoaJuridica;

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
     * @return the idOrigem
     */
    public Long getIdOrigem() {
        return idOrigem;
    }

    /**
     * @param idOrigem the idOrigem to set
     */
    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }

    /**
     * @return the pessoaJuridica
     */
    public HistoricoTitularidadeCotaPessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    /**
     * @param pessoaJuridica the pessoaJuridica to set
     */
    public void setPessoaJuridica(HistoricoTitularidadeCotaPessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

}
