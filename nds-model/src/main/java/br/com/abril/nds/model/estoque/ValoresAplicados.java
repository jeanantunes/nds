package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ValoresAplicados implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1373107779645109174L;

	public ValoresAplicados() {
		super();
	}
	
	public ValoresAplicados(BigDecimal precoVenda, BigDecimal precoComDesconto, BigDecimal valorDesconto) {
		this.precoVenda = precoVenda;
		this.precoComDesconto = precoComDesconto;
		this.valorDesconto = valorDesconto;
	}
	
	@Column(name = "PRECO_VENDA")
	private BigDecimal precoVenda;
	
	@Column(name = "PRECO_COM_DESCONTO")
	private BigDecimal precoComDesconto;
	
	@Column(name = "VALOR_DESCONTO")
	private BigDecimal valorDesconto;
	
	
	/**
	 * Getters e Setters 
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

}
