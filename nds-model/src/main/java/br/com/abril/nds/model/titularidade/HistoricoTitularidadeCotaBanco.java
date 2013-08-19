package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Representa as informações do banco no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaBanco implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Número do banco
     */
    @Column(name = "BANCO_NUMERO")
    private String numeroBanco;
    
    /**
     * Nome do banco
     */
    @Column(name = "BANCO_NOME")
    private String nome;
    
    /**
     * Agencia do banco
     */
    @Column(name = "BANCO_AGENCIA")
    private Long agencia;
    
    /**
     * Dígito verificador da agência
     */
    @Column(name = "BANCO_DV_AGENCIA")
    private String dvAgencia;
    
    /**
     * Número da conta
     */
    @Column(name = "BANCO_CONTA")
    private Long conta;
    
    /**
     * Dígito verificador da conta
     */
    @Column(name = "BANCO_DV_CONTA")
    private String dvConta;
    
    public HistoricoTitularidadeCotaBanco() {
    }
    
    public HistoricoTitularidadeCotaBanco(String numeroBanco, String nome,
            Long agencia, String dvAgencia, Long conta, String dvConta) {
        this.numeroBanco = numeroBanco;
        this.nome = nome;
        this.agencia = agencia;
        this.dvAgencia = dvAgencia;
        this.conta = conta;
        this.dvConta = dvConta;
    }


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