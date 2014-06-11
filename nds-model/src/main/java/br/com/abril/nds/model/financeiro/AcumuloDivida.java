package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@SuppressWarnings("serial")
@Entity
@Table(name = "ACUMULO_DIVIDA")
@SequenceGenerator(name="ACUMULO_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class AcumuloDivida implements Serializable {
	
	@Id
	@GeneratedValue(generator = "ACUMULO_DIVIDA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name="DIVIDA_ID")
	private Divida dividaAnterior;
	
	@OneToOne
	@JoinColumn(name="MOV_PENDENTE_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroPendente; 
	
	@OneToOne
	@JoinColumn(name="MOV_JUROS_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroJuros; 
	
	@OneToOne(optional=false)
	@JoinColumn(name="MOV_MULTA_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroMulta; 

	@OneToOne
	@JoinColumn(name = "USUARIO_ID", nullable = false)
	private Usuario responsavel;
	
	@OneToOne
	@JoinColumn(name="COTA_ID", nullable = false)
	private Cota cota;
	
	@Column(name="NUMERO_ACUMULO", nullable=false, columnDefinition="default 0")
	private BigInteger numeroAcumulo;
	
	@Column(name="DATA_CRIACAO", nullable=false)
	private Date dataCriacao;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusInadimplencia status;
	
	
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
	 * @return the dividaAnterior
	 */
	public Divida getDividaAnterior() {
		return dividaAnterior;
	}

	/**
	 * @param dividaAnterior the dividaAnterior to set
	 */
	public void setDividaAnterior(Divida dividaAnterior) {
		this.dividaAnterior = dividaAnterior;
	}

	/**
	 * @return the movimentoFinanceiroPendente
	 */
	public MovimentoFinanceiroCota getMovimentoFinanceiroPendente() {
		return movimentoFinanceiroPendente;
	}

	/**
	 * @param movimentoFinanceiroPendente the movimentoFinanceiroPendente to set
	 */
	public void setMovimentoFinanceiroPendente(
			MovimentoFinanceiroCota movimentoFinanceiroPendente) {
		this.movimentoFinanceiroPendente = movimentoFinanceiroPendente;
	}

	/**
	 * @return the movimentoFinanceiroJuros
	 */
	public MovimentoFinanceiroCota getMovimentoFinanceiroJuros() {
		return movimentoFinanceiroJuros;
	}

	/**
	 * @param movimentoFinanceiroJuros the movimentoFinanceiroJuros to set
	 */
	public void setMovimentoFinanceiroJuros(
			MovimentoFinanceiroCota movimentoFinanceiroJuros) {
		this.movimentoFinanceiroJuros = movimentoFinanceiroJuros;
	}

	/**
	 * @return the movimentoFinanceiroMulta
	 */
	public MovimentoFinanceiroCota getMovimentoFinanceiroMulta() {
		return movimentoFinanceiroMulta;
	}

	/**
	 * @param movimentoFinanceiroMulta the movimentoFinanceiroMulta to set
	 */
	public void setMovimentoFinanceiroMulta(
			MovimentoFinanceiroCota movimentoFinanceiroMulta) {
		this.movimentoFinanceiroMulta = movimentoFinanceiroMulta;
	}

	/**
	 * @return the numeroAcumulo
	 */
	public BigInteger getNumeroAcumulo() {
		return numeroAcumulo;
	}

	/**
	 * @param numeroAcumulo the numeroAcumulo to set
	 */
	public void setNumeroAcumulo(BigInteger numeroAcumulo) {
		this.numeroAcumulo = numeroAcumulo;
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the status
	 */
	public StatusInadimplencia getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusInadimplencia status) {
		this.status = status;
	}

	/**
	 * @return the responsavel
	 */
	public Usuario getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}	
}
