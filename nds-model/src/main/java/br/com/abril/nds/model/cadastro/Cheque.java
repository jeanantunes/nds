/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name="CHEQUE")
@SequenceGenerator(name="CHEQUE_SEQ",allocationSize=1,initialValue=1)
public class Cheque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7324670787014949189L;
	
	
	@Id
	@GeneratedValue(generator="CHEQUE_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name = "NUMERO_BANCO", nullable = false)
	private String numeroBanco;
	
	@Column(name = "NOME_BANCO", nullable = false)
	private String nomeBanco;
	
	@Column(name = "AGENCIA", nullable = false)
	private Long agencia;
	
	@Column(name = "DV_AGENCIA", nullable=false)
	private String dvAgencia;
	
	@Column(name = "CONTA", nullable = false)
	private Long conta;
	
	@Column(name = "DV_CONTA", nullable=false)
	private String dvConta;
	
	@Column(name="VALOR", nullable=false)
	private BigDecimal valor;
	
	@Column(name="NUMERO_CHEQUE", nullable=false)
	private String numeroCheque;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_EMISSAO", nullable=false)
	private Date emissao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_VALIDADE",nullable=false)
	private Date validade;
	
	@Column(name="CORRENTISTA", nullable=false)
	private String correntista;
	
	@JsonIgnore
	@OneToOne(mappedBy="cheque", orphanRemoval=true)
	private ChequeImage chequeImage;
	
	public Cheque(long idCheque) {
		this.id = idCheque;
	}
	
	
	public Cheque() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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


	/**
	 * @return the chequeImage
	 */
	public ChequeImage getChequeImage() {
		return chequeImage;
	}


	/**
	 * @param chequeImage the chequeImage to set
	 */
	public void setChequeImage(ChequeImage chequeImage) {
		this.chequeImage = chequeImage;
	}
}
