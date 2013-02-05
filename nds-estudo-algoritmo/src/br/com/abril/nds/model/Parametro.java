package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Parametro {

	private List<Cota> cotas;
	private BigDecimal pacotePadrao;

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	public BigDecimal getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(BigDecimal pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
}
