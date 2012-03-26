package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

@MappedSuperclass
public abstract class AbstractMovimentoFinanceiro extends Movimento {

	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_MOVIMENTO_ID")
	private TipoMovimentoFinanceiro tipoMovimento;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "BAIXA_AUTOMATICA_ID")
	private BaixaAutomatica baixaAutomatica;
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public TipoMovimentoFinanceiro getTipoMovimento() {
		return tipoMovimento;
	}
	
	public void setTipoMovimento(TipoMovimentoFinanceiro tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public BaixaAutomatica getBaixaAutomatica() {
		return baixaAutomatica;
	}

	public void setBaixaAutomatica(BaixaAutomatica baixaAutomatica) {
		this.baixaAutomatica = baixaAutomatica;
	}
	
}
