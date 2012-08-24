package br.com.abril.nds.model.titularidade;

import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

/**
 * Representa a garantia do tipo "FIADOR" no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("FIADOR")
public class HistoricoTitularidadeCotaFiador extends HistoricoTitularidadeCotaGarantia{
    
    private static final long serialVersionUID = 1L;

    /**
     * Nome do fiador
     */
    @Column(name = "FIADOR_NOME")
    private String nome;
    
    /**
     * CPF/CNPJ do fiador
     */
    @Column(name = "FIADOR_CPF_CNPJ")
    private String cpfCnpj;
    
    /**
     * Endereço do fiador
     */
    @Column(name = "FIADOR_ENDERECO")
    private String endereco;
    
    /**
     * Telefone do fiador
     */
    @Column(name = "FIADOR_TELEFONE")
    private String telefone;
    
    /**
     * Garantias do fiador
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_FIADOR_GARANTIA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FIADOR_ID")})
    private Collection<HistoricoTitularidadeCotaFiadorGarantia> garantias;


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
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the garantias
     */
    public Collection<HistoricoTitularidadeCotaFiadorGarantia> getGarantias() {
        return garantias;
    }

    /**
     * @param garantias the garantias to set
     */
    public void setGarantias(Collection<HistoricoTitularidadeCotaFiadorGarantia> garantias) {
        this.garantias = garantias;
    }
    
}
