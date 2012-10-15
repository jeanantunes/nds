package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PoliticaSuspensao implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5431807534550378907L;

	@Column(name = "NUM_ACUMULO_DIVIDA",nullable=true)
	private Integer numeroAcumuloDivida;
	
	@Column(name = "VALOR_SUSPENSAO",nullable=true)
	private BigDecimal valor;
	
	public Integer getNumeroAcumuloDivida() {
		return numeroAcumuloDivida;
	}
	
	public void setNumeroAcumuloDivida(Integer numeroAcumuloDivida) {
		this.numeroAcumuloDivida = numeroAcumuloDivida;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getNumeroAcumuloDividaWrapped() {
		return numeroAcumuloDivida;
	}

}
