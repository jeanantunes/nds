package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;

/**
 * DTO com as informações da Pessoa Jurídica utilizada na chamada de encalhe de
 * devolução de produtos ao fornecedor
 * 
 * @author francisco.garcia
 * 
 */
public class PessoaJuridicaChamadaEncalheFornecedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String razaoSocial;
    
    private String cnpj;
    
    private String inscricaoEstadual;
    
    private String logradouro;
    
    private Integer numero;
    
    private String cidade;
    
    private String uf;
    
    private String cep;
    
    public PessoaJuridicaChamadaEncalheFornecedorDTO(Long id,
            String razaoSocial, String cnpj, String inscricaoEstadual,
            String logradouro, Integer numero, String cidade, String uf,
            String cep) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @return the logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * @return the numero
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

}
