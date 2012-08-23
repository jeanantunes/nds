package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_FORNECEDOR")
@SequenceGenerator(name="HIST_TIT_FORN_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaFornecedor implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_FORN_SEQ")
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
    private HistoricoTitularidadePessoaJuridica pessoaJuridica;

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
    public HistoricoTitularidadePessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    /**
     * @param pessoaJuridica the pessoaJuridica to set
     */
    public void setPessoaJuridica(HistoricoTitularidadePessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

}
