package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ContaDepositoCaucaoLiquida implements Serializable {

	private static final long serialVersionUID = 195620034968325207L;

	@Column(name="NUMERO_BANCO", nullable=true)
	private Integer numeroBanco;
	
	@Column(name="BANCO",nullable=true)
	private String nomebanco;
	
	@Column(name="NUMERO_AGENCIA",nullable=true)
	private Integer numeroAgencia;
	
	@Column(name="NUMERO_CONTA",nullable=true)
	private Integer numeroContaCorrente;
	
	@Column(name="CORRENTISTA",nullable=true)
	private String nomeCorrentista;

	/**
	 * @return the numeroBanco
	 */
	public Integer getNumeroBanco() {
		return numeroBanco;
	}

	/**
	 * @param numeroBanco the numeroBanco to set
	 */
	public void setNumeroBanco(Integer numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	/**
	 * @return the nomebanco
	 */
	public String getNomebanco() {
		return nomebanco;
	}

	/**
	 * @param nomebanco the nomebanco to set
	 */
	public void setNomebanco(String nomebanco) {
		this.nomebanco = nomebanco;
	}

	/**
	 * @return the numeroAgencia
	 */
	public Integer getNumeroAgencia() {
		return numeroAgencia;
	}

	/**
	 * @param numeroAgencia the numeroAgencia to set
	 */
	public void setNumeroAgencia(Integer numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	/**
	 * @return the numeroContaCorrente
	 */
	public Integer getNumeroContaCorrente() {
		return numeroContaCorrente;
	}

	/**
	 * @param numeroContaCorrente the numeroContaCorrente to set
	 */
	public void setNumeroContaCorrente(Integer numeroContaCorrente) {
		this.numeroContaCorrente = numeroContaCorrente;
	}

	/**
	 * @return the nomeCorrentista
	 */
	public String getNomeCorrentista() {
		return nomeCorrentista;
	}

	/**
	 * @param nomeCorrentista the nomeCorrentista to set
	 */
	public void setNomeCorrentista(String nomeCorrentista) {
		this.nomeCorrentista = nomeCorrentista;
	}
	
	
}
