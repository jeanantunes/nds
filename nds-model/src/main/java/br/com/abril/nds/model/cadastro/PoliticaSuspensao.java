package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PoliticaSuspensao {
	
	@Column(name = "NUM_ACUMULO_DIVIDA")
	private int numeroAcumuloDivida;
	
	@Column(name = "VALOR_SUSPENSAO")
	private BigDecimal valor;
	
	public int getNumeroAcumuloDivida() {
		return numeroAcumuloDivida;
	}
	
	public void setNumeroAcumuloDivida(int numeroAcumuloDivida) {
		this.numeroAcumuloDivida = numeroAcumuloDivida;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
