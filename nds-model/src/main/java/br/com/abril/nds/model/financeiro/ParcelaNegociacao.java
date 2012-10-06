package br.com.abril.nds.model.financeiro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
}