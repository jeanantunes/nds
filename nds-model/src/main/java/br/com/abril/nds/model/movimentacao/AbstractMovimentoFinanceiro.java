package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.financeiro.BaixaCobranca;

@MappedSuperclass
public abstract class AbstractMovimentoFinanceiro extends Movimento {

	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	
	/*
	 * TODO: Aguardando definições referente a negociação.
	 * Campos afetados: parcelas e prazo
	 * 
	 */
	@Column(name = "PARCELAS", nullable = true)
	private Integer parcelas;
	
	@Column(name = "PRAZO", nullable = true)
	private Integer prazo;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "BAIXA_COBRANCA_ID")
	private BaixaCobranca baixaCobranca;
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public BaixaCobranca getBaixaCobranca() {
		return baixaCobranca;
	}

	public void setBaixaCobranca(BaixaCobranca baixaCobranca) {
		this.baixaCobranca = baixaCobranca;
	}
	
}
