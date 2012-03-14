package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

@MappedSuperclass
public abstract class AbstractMovimentoFinanceiro extends Movimento {

	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_MOVIMENTO_ID")
	private TipoMovimentoFinanceiro tipoMovimento;
	
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
	
}
