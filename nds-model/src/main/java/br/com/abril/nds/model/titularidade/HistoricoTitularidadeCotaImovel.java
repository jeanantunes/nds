package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoGarantia;

/**
 * Representa a garantia do tipo "IMOVEL" no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("IMOVEL")
public class HistoricoTitularidadeCotaImovel extends
        HistoricoTitularidadeCotaGarantia {

    private static final long serialVersionUID = 1L;

    /**
     * Nome do proprietário do imóvel
     */
    @Column(name = "IMOVEL_PROPRIETARIO")
    private String proprietario;

    /**
     * Endereço do imóvel
     */
    @Column(name = "IMOVEL_ENDERECO")
    private String endereco;

    /**
     * Número de registro do imóvel
     */
    @Column(name = "IMOVEL_NUMERO_REGISTRO")
    private String numeroRegistro;

    /**
     * Valor do imóvel
     */
    @Column(name = "IMOVEL_VALOR")
    private BigDecimal valor;

    @Column(name = "IMOVEL_OBSERVACAO")
    private String observacao;
    
    public HistoricoTitularidadeCotaImovel() {
        this.tipoGarantia = TipoGarantia.IMOVEL;
    }

    /**
     * @return the proprietario
     */
    public String getProprietario() {
        return proprietario;
    }

    /**
     * @param proprietario
     *            the proprietario to set
     */
    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco
     *            the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the numeroRegistro
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * @param numeroRegistro
     *            the numeroRegistro to set
     */
    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor
     *            the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao
     *            the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
