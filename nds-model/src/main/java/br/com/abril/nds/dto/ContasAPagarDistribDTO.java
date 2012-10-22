package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContasAPagarDistribDTO implements Serializable {

	private static final long serialVersionUID = -4541737247245237031L;
	
	private String nome;
	private BigDecimal total;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
