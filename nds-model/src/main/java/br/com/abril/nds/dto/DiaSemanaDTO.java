package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Calendar;

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
		
		switch (numDia) {
			case Calendar.SUNDAY:
				diaSemana = DiaSemana.DOMINGO;
			break;
			case Calendar.MONDAY:
				diaSemana = DiaSemana.SEGUNDA_FEIRA;
			break;
			case Calendar.TUESDAY:
				diaSemana = DiaSemana.TERCA_FEIRA;
			break;
			case Calendar.WEDNESDAY:
				diaSemana = DiaSemana.QUARTA_FEIRA;
			break;
			case Calendar.THURSDAY:
				diaSemana = DiaSemana.QUINTA_FEIRA;
			break;
			case Calendar.FRIDAY:
				diaSemana = DiaSemana.SEXTA_FEIRA;
			break;
			case Calendar.SATURDAY:
				diaSemana = DiaSemana.SABADO;
			break;
			default:
				throw new IllegalArgumentException("Número do dia inválido.");
		}
		
		this.numDia = numDia;
	}
}
