package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public class NegociacaoDividaDetalheVO implements Serializable{

	private static final long serialVersionUID = 6521983915168304492L;
	
	private Date data;
	
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
	
	private BigDecimal valor;
		
	private String observacao;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoMovimentoFinanceiro getTipoMovimentoFinanceiro() {
		return tipoMovimentoFinanceiro;
	}

	public void setTipoMovimentoFinanceiro(TipoMovimentoFinanceiro tipoMovimentoFinanceiro) {
		this.tipoMovimentoFinanceiro = tipoMovimentoFinanceiro;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}