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
	
	public ValoresAplicados(BigDecimal valor, BigDecimal desconto) {
		this.valor = valor;
		this.desconto = desconto;
	}
	
	@Column(name = "VALOR_APLICADO")
	private BigDecimal valor;
	
	@Column(name = "DESCONTO")
	private BigDecimal desconto;

	/**
	 * Getters e Setters 
	 */
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

}
