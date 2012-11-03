package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.DiaSemana;

public class DiaSemanaDTO implements Serializable {

	private static final long serialVersionUID = 4645909304669568228L;

	private DiaSemana diaSemana;
	private int numDia;
	
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public int getNumDia() {
		return numDia;
	}


	public void setNumDia(int numDia) {
		diaSemana = DiaSemana.values()[numDia];
		this.numDia = numDia;
	}
}
