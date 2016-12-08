package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ImpressaoNegociacaoParecelaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8408093325685999145L;

	private Date dataVencimento;
	
	private String numeroCheque;
	
	private BigDecimal valor;
	
	private BigDecimal parcelaTotal;
	
	private BigDecimal encagos;
	
	private boolean ativarAoPagar;
	
	private int numeroParcela;
	
	private String nossoNumeroCobranca;
	
	private String dataEmissaoCobranca;
	
	private String dataVencimentoCobranca;
	
	private String valorCobranca;	
	
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getParcelaTotal() {
		return parcelaTotal;
	}

	public void setParcelaTotal(BigDecimal parcelaTotal) {
		this.parcelaTotal = parcelaTotal;
	}

	public BigDecimal getEncagos() {
		return encagos;
	}

	public void setEncagos(BigDecimal encagos) {
		this.encagos = encagos;
	}

	public boolean isAtivarAoPagar() {
		return ativarAoPagar;
	}

	public void setAtivarAoPagar(boolean ativarAoPagar) {
		this.ativarAoPagar = ativarAoPagar;
	}

	public int getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(int numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public String getNossoNumeroCobranca() {
		return nossoNumeroCobranca;
	}

	public void setNossoNumeroCobranca(String nossoNumeroCobranca) {
		this.nossoNumeroCobranca = nossoNumeroCobranca;
	}

	public String getDataEmissaoCobranca() {
		return dataEmissaoCobranca;
	}

	public void setDataEmissaoCobranca(String dataEmissaoCobranca) {
		this.dataEmissaoCobranca = dataEmissaoCobranca;
	}

	public String getDataVencimentoCobranca() {
		return dataVencimentoCobranca;
	}

	public void setDataVencimentoCobranca(String dataVencimentoCobranca) {
		this.dataVencimentoCobranca = dataVencimentoCobranca;
	}

	public String getValorCobranca() {
		return valorCobranca;
	}

	public void setValorCobranca(String valorCobranca) {
		this.valorCobranca = valorCobranca;
	}
}