package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO com as informações de imóvel
 * 
 * @author francisco.garcia
 *
 */
public class ImovelDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String proprietario;
	
	private String endereco;
	
	private String numeroRegistro;
	
	private BigDecimal valor;
	
	private String observacao;
	

    public ImovelDTO(String proprietario, String endereco,
            String numeroRegistro, BigDecimal valor, String observacao) {
        this.proprietario = proprietario;
        this.endereco = endereco;
        this.numeroRegistro = numeroRegistro;
        this.valor = valor;
        this.observacao = observacao;
    }

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
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
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
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


}
