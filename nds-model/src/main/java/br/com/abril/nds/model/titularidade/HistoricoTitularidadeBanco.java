package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Representa as informações do banco no histórico de
 * titularidade da cota
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class HistoricoTitularidadeBanco {
    
    @Column(name = "BANCO_NUMERO")
    private String numeroBanco;
    
    @Column(name = "BANCO_NOME")
    private String nome;
    
    @Column(name = "BANCO_AGENCIA")
    private Long agencia;
    
    @Column(name = "BANCO_DV_AGENCIA")
    private String dvAgencia;
    
    @Column(name = "BANCO_CONTA")
    private Long conta;
    
    @Column(name = "BANCO_DV_CONTA")
    private String dvConta;

    /**
     * @return the numeroBanco
     */
    public String getNumeroBanco() {
        return numeroBanco;
    }

    /**
     * @param numeroBanco the numeroBanco to set
     */
    public void setNumeroBanco(String numeroBanco) {
        this.numeroBanco = numeroBanco;
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
     * @return the agencia
     */
    public Long getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the dvAgencia
     */
    public String getDvAgencia() {
        return dvAgencia;
    }

    /**
     * @param dvAgencia the dvAgencia to set
     */
    public void setDvAgencia(String dvAgencia) {
        this.dvAgencia = dvAgencia;
    }

    /**
     * @return the conta
     */
    public Long getConta() {
        return conta;
    }

    /**
     * @param conta the conta to set
     */
    public void setConta(Long conta) {
        this.conta = conta;
    }

    /**
     * @return the dvConta
     */
    public String getDvConta() {
        return dvConta;
    }

    /**
     * @param dvConta the dvConta to set
     */
    public void setDvConta(String dvConta) {
        this.dvConta = dvConta;
    }
    
    

    
}