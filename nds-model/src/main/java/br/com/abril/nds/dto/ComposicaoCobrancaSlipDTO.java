package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ComposicaoCobrancaSlipDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235698L;
	
	private Long idMovimentoFinanceiro;
	private String descricao;
	private String operacaoFinanceira;
	private BigDecimal valor;
	
	public ComposicaoCobrancaSlipDTO() {
		
	}
	
	/**
	 * @param descricao
	 * @param operacaoFinanceira
	 * @param valor
	 */
	public ComposicaoCobrancaSlipDTO(String descricao,
			String operacaoFinanceira, BigDecimal valor, Long idMovimentoFinanceiro) {
		super();
		this.idMovimentoFinanceiro = idMovimentoFinanceiro;
		this.descricao = descricao;
		this.operacaoFinanceira = operacaoFinanceira;
		this.valor = valor;
	}
	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * @return the operacaoFinanceira
	 */
	public String getOperacaoFinanceira() {
		return operacaoFinanceira;
	}
	
	/**
	 * @param operacaoFinanceira the operacaoFinanceira to set
	 */
	public void setOperacaoFinanceira(String operacaoFinanceira) {
		this.operacaoFinanceira = operacaoFinanceira;
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
	 * @return the idMovimentoFinanceiro
	 */
	public Long getIdMovimentoFinanceiro() {
		return idMovimentoFinanceiro;
	}

	/**
	 * @param idMovimentoFinanceiro the idMovimentoFinanceiro to set
	 */
	public void setIdMovimentoFinanceiro(Long idMovimentoFinanceiro) {
		this.idMovimentoFinanceiro = idMovimentoFinanceiro;
	}
	
}
