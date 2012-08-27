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
 * Entidade que representa o sócio no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_SOCIO")
@SequenceGenerator(name = "HIST_TIT_COTA_SOCIO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaSocio implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_SOCIO_SEQ")
    @Column(name = "ID")
    private Long id;
    
    /**
     * Nome do sócio
     */
    @Column(name = "NOME", nullable = false)
    private String nome;
    
    /**
     * Flag indicando se é o sócio principal
     */
    @Column(name = "PRINCIPAL_SOCIO")
    private boolean principal;
    
    /**
     * Cargo do sócio
     */
    @Column(name = "CARGO")
    private String cargo;
    
    /**
     * Endereço do sócio
     */
    @Embedded
    private HistoricoTitularidadeCotaEndereco endereco;
    
    /**
     * Telefone do sócio
     */
    @Embedded
    private HistoricoTitularidadeCotaTelefone telefone;

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
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the principal
     */
    public boolean isPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo the cargo to set
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the endereco
     */
    public HistoricoTitularidadeCotaEndereco getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(HistoricoTitularidadeCotaEndereco endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the telefone
     */
    public HistoricoTitularidadeCotaTelefone getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(HistoricoTitularidadeCotaTelefone telefone) {
        this.telefone = telefone;
    }
    

}
