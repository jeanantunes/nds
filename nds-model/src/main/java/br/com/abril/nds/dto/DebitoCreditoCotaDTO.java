package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.financeiro.OperacaoFinaceira;

public class DebitoCreditoCotaDTO implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	private OperacaoFinaceira tipoLancamento;
	private BigDecimal valor;
	private Date dataLancamento;
	private Date dataVencimento;
	private Integer numeroCota;
	
	private String observacoes;
	
	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public OperacaoFinaceira getTipoLancamento() {
		return tipoLancamento;
	}
	
	public String getTipoLancamentoDescricao() {
		return (tipoLancamento!=null)? tipoLancamento.getDescricao():null;
	}

	public void setTipoLancamento(OperacaoFinaceira tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the observacoes
	 */
	public String getObservacoes() {
		return observacoes;
	}

	/**
	 * @param observacoes the observacoes to set
	 */
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	
}
