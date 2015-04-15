package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.cadastro.TipoEndereco;

/**
 * Entidade para os endereços utilizados no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaEndereco implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Bairro do endereço
     */
    @Column(name = "ENDERECO_BAIRRO", length = 60)
    private String bairro;

    /**
     * Cep do endereço
     */
    @Column(name = "ENDERECO_CEP", length = 9)
    private String cep;

    /**
     * Código da cidade IBGE
     */
    @Column(name = "ENDERECO_CODIGO_CIDADE_IBGE", length = 7)
    private Integer codigoCidadeIBGE;

    /**
     * Cidade do endereço
     */
    @Column(name = "ENDERECO_CIDADE", length = 60)
    private String cidade;

    /**
     * Complemento do endereço
     */
    @Column(name = "ENDERECO_COMPLEMENTO", length = 60)
    private String complemento;

    /**
     * Tipo do logradouro do endereço
     */
    @Column(name = "ENDERECO_TIPO_LOGRADOURO")
    private String tipoLogradouro;

    /**
     * Logradouro do endereço 
     */
    @Column(name = "ENDERECO_LOGRADOURO", length = 60)
    private String logradouro;

    /**
     * Número do endereço
     */
    @Column(name = "ENDERECO_NUMERO", length = 60)
    private String numero;

    /**
     * UF do endereço
     */
    @Column(name = "ENDERECO_UF", length = 2)
    private String uf;

    /**
     * Código da UF do endereço
     */
    @Column(name = "ENDERECO_CODIGO_UF", length = 2)
    private Integer codigoUf;
    
    /**
     * Tipo do endereço
     */
    @Column(name = "ENDERECO_TIPO_ENDERECO")
    @Enumerated(EnumType.STRING)
    private TipoEndereco tipoEndereco;
   
    /**
     * Flag indicando se é o endereço principal
     */
    @Column(name = "ENDERECO_PRINCIPAL")
    private boolean principal;
    
    public HistoricoTitularidadeCotaEndereco() {
    }

    public HistoricoTitularidadeCotaEndereco(
            String bairro, String cep, Integer codigoCidadeIBGE, String cidade,
            String complemento, String tipoLogradouro, String logradouro,
            String numero, String uf, Integer codigoUf,
            TipoEndereco tipoEndereco, boolean principal) {
        this.bairro = bairro;
        this.cep = cep;
        this.codigoCidadeIBGE = codigoCidadeIBGE;
        this.cidade = cidade;
        this.complemento = complemento;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.uf = uf;
        this.codigoUf = codigoUf;
        this.tipoEndereco = tipoEndereco;
        this.principal = principal;
    }


    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the codigoCidadeIBGE
     */
    public Integer getCodigoCidadeIBGE() {
        return codigoCidadeIBGE;
    }

    /**
     * @param codigoCidadeIBGE the codigoCidadeIBGE to set
     */
    public void setCodigoCidadeIBGE(Integer codigoCidadeIBGE) {
        this.codigoCidadeIBGE = codigoCidadeIBGE;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the tipoLogradouro
     */
    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    /**
     * @param tipoLogradouro the tipoLogradouro to set
     */
    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    /**
     * @return the logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * @param logradouro the logradouro to set
     */
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * @return the codigoUf
     */
    public Integer getCodigoUf() {
        return codigoUf;
    }

    /**
     * @param codigoUf the codigoUf to set
     */
    public void setCodigoUf(Integer codigoUf) {
        this.codigoUf = codigoUf;
    }

    /**
     * @return the tipoEndereco
     */
    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    /**
     * @param tipoEndereco the tipoEndereco to set
     */
    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
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

}
