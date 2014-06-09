package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO com as informações do cheque caução
 * 
 * @author francisco.garcia
 *
 */
public class ChequeCaucaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String numeroBanco;
	
	private String nomeBanco;
	
	private Long agencia;
	
	private String dvAgencia;
	
	private Long conta;
	
	private String dvConta;
	
	private BigDecimal valor;
	
	private String numeroCheque;
	
	private Date emissao;
	
	private Date validade;
	
	private String correntista;
	
	public ChequeCaucaoDTO(){
		
	}

    /**
	 * @param numeroBanco
	 * @param nomeBanco
	 * @param agencia
	 * @param dvAgencia
	 * @param conta
	 * @param dvConta
	 * @param valor
	 * @param numeroCheque
	 * @param emissao
	 * @param validade
	 * @param correntista
	 */
	public ChequeCaucaoDTO(String numeroBanco, String nomeBanco, Long agencia,
			String dvAgencia, Long conta, String dvConta, BigDecimal valor,
			String numeroCheque, Date emissao, Date validade, String correntista) {
		
		super();
		
		this.numeroBanco = numeroBanco;
		this.nomeBanco = nomeBanco;
		this.agencia = agencia;
		this.dvAgencia = dvAgencia;
		this.conta = conta;
		this.dvConta = dvConta;
		this.valor = valor;
		this.numeroCheque = numeroCheque;
		this.emissao = emissao;
		this.validade = validade;
		this.correntista = correntista;
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
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @param nomeBanco the nomeBanco to set
     */
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
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
     * @return the numeroCheque
     */
    public String getNumeroCheque() {
        return numeroCheque;
    }

    /**
     * @param numeroCheque the numeroCheque to set
     */
    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    /**
     * @return the emissao
     */
    public Date getEmissao() {
        return emissao;
    }

    /**
     * @param emissao the emissao to set
     */
    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    /**
     * @return the validade
     */
    public Date getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Date validade) {
        this.validade = validade;
    }

    /**
     * @return the correntista
     */
    public String getCorrentista() {
        return correntista;
    }

    /**
     * @param correntista the correntista to set
     */
    public void setCorrentista(String correntista) {
        this.correntista = correntista;
    }
	
}
