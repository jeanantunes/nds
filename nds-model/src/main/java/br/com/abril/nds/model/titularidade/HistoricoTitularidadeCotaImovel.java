package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Representa a garantia do tipo "IMOVEL" no histórico de
 * titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("IMOVEL")
public class HistoricoTitularidadeCotaImovel extends HistoricoTitularidadeCotaGarantia {
    
    @Column(name="IMOVEL_PROPRIETARIO", nullable=false)
    private String proprietario;
    
    @Column(name="IMOVEL_ENDERECO", nullable=false)
    private String endereco;
    
    @Column(name="IMOVEL_NUMERO_REGISTRO", nullable=false)
    private String numeroRegistro;
    
    @Column(name="IMOVEL_VALOR", nullable=false)
    private Double valor;
    
    @Column(name="IMOVEL_OBSERVACAO", nullable=false)
    private String observacao;

    /**
     * @return the proprietario
     */
    public String getProprietario() {
        return proprietario;
    }

    /**
     * @param proprietario the proprietario to set
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
     * @param endereco the endereco to set
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
     * @param numeroRegistro the numeroRegistro to set
     */
    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
