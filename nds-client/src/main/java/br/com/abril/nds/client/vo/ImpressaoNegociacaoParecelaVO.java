package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ImpressaoNegociacaoParecelaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8408093325685999145L;

	@Export(label = "Vencimento")
	private Date dataVencimento;
	
	@Export(label = "Num. Cheque")
	private String numeroCheque;
	
	@Export(label = "Parcela/Valor R$")
	private BigDecimal valor;
	
	@Export(label = "Parc. Total")
	private BigDecimal parcelaTotal;
	
	@Export(label = "Encargos")
	private BigDecimal encagos;

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
}