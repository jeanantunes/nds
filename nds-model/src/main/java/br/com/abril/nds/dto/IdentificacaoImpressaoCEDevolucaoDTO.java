package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * Identificação do fornecedor/distribuidor na 
 * impressão de CE Devolução 
 * @author francisco.garcia
 *
 */
public class IdentificacaoImpressaoCEDevolucaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    
    private EnderecoDTO endereco;
    
    private String cnpj;
    
    private String inscricaoEstadual;
 
    public IdentificacaoImpressaoCEDevolucaoDTO(String nome,
            EnderecoDTO endereco, String cnpj, String inscricaoEstadual) {
        this.nome = nome;
        this.endereco = endereco;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
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
     * @return the endereco
     */
    public EnderecoDTO getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual the inscricaoEstadual to set
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }
    
}