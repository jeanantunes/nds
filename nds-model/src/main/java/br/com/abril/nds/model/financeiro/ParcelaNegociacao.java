package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "PARCELA_NEGOCIACAO_SEQ", initialValue = 1, allocationSize = 1)
@Table(name = "PARCELA_NEGOCIACAO")
public class ParcelaNegociacao {

	@Id
	@GeneratedValue(generator = "PARCELA_NEGOCIACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "MOVIMENTO_FINANCEIRO_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroCota;
	
	@Column(name = "NUMERO_CHEQUE")
	private String numeroCheque;
	
	@Column(name = "DATA_VENCIMENTO")
	private Date dataVencimento;
	
	@Column(name = "ENCARGOS")
	private BigDecimal encargos;
	
	@ManyToOne
	@JoinColumn(name = "NEGOCIACAO_ID")
	private Negociacao negociacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MovimentoFinanceiroCota getMovimentoFinanceiroCota() {
		return movimentoFinanceiroCota;
	}

	public void setMovimentoFinanceiroCota(
			MovimentoFinanceiroCota movimentoFinanceiroCota) {
		this.movimentoFinanceiroCota = movimentoFinanceiroCota;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	/**
	 * @return the negociacao
	 */
	public Negociacao getNegociacao() {
		return negociacao;
	}

	/**
	 * @param negociacao the negociacao to set
	 */
	public void setNegociacao(Negociacao negociacao) {
		this.negociacao = negociacao;
	}
	
	
}