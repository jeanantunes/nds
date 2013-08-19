package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class Duplicata{
	
	public Duplicata(){}
	
	private String numeroDuplicata;
	
	private Date vencimentoDuplicata;
	
	private BigDecimal valorDuplicata;

	public String getNumeroDuplicata() {
		return numeroDuplicata;
	}

	public void setNumeroDuplicata(String numeroDuplicata) {
		this.numeroDuplicata = numeroDuplicata;
	}

	public Date getVencimentoDuplicata() {
		return vencimentoDuplicata;
	}

	public void setVencimentoDuplicata(Date vencimentoDuplicata) {
		this.vencimentoDuplicata = vencimentoDuplicata;
	}

	public BigDecimal getValorDuplicata() {
		return valorDuplicata;
	}

	public void setValorDuplicata(BigDecimal valorDuplicata) {
		this.valorDuplicata = valorDuplicata;
	}
}
