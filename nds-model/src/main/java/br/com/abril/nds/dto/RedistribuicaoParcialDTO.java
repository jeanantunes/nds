package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class RedistribuicaoParcialDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idLancamentoRedistribuicao;
	
	private Long idPeriodo;
	
	private Integer numeroPeriodo;
	
	private Integer numeroLancamento;
	
	private Date dataLancamento;
	
	private Date dataRecolhimento;
	
	private String nomeProduto;

	public Long getIdLancamentoRedistribuicao() {
		return idLancamentoRedistribuicao;
	}

	public void setIdLancamentoRedistribuicao(Long idLancamentoRedistribuicao) {
		this.idLancamentoRedistribuicao = idLancamentoRedistribuicao;
	}

	public Long getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}

	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
	}

	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}

	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
}
