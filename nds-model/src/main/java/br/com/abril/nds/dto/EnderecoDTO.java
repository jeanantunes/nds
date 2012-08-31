package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Endereco;

/**
 * DTO com as informações de endereço
 * 
 * @author francisco.garcia
 *
 */
public class EnderecoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer codigoBairro;

    private String bairro;

    private String cep;

    private Integer codigoCidadeIBGE;

    private String cidade;

    private String complemento;

    private String tipoLogradouro;

    private String logradouro;

    private String numero;

    private String uf;

    private Integer codigoUf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(Integer codigoBairro) {
        this.codigoBairro = codigoBairro;
    }

    public Integer getCodigoCidadeIBGE() {
        return codigoCidadeIBGE;
    }

    public void setCodigoCidadeIBGE(Integer codigoCidadeIBGE) {
        this.codigoCidadeIBGE = codigoCidadeIBGE;
    }

    public Integer getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(Integer codigoUf) {
        this.codigoUf = codigoUf;
    }
    
    public static EnderecoDTO fromEndereco(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(endereco.getId());
        dto.setTipoLogradouro(endereco.getLogradouro());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCodigoBairro(endereco.getCodigoBairro());
        dto.setCep(endereco.getCep());
        dto.setCidade(endereco.getCidade());
        dto.setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE());
        dto.setUf(endereco.getUf());
        dto.setCodigoUf(endereco.getCodigoUf());
        return dto;
    }

}