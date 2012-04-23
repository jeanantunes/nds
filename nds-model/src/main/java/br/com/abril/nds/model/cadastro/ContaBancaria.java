package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Informações bancárias da cota
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class ContaBancaria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "NUMERO_BANCO")
	private String numeroBanco;
	
	@Column(name = "NOME_BANCO")
	private String nomeBanco;
	
	@Column(name = "AGENCIA_BANCO")
	private Long agencia;
	
	@Column(name = "DV_AGENCIA_BANCO")
	private String dvAgencia;
	
	@Column(name = "CONTA_BANCO")
	private Long conta;
	
	@Column(name = "DV_CONTA_BANCO")
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

}
