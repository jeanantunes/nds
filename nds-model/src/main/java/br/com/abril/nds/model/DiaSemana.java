package br.com.abril.nds.model;

import java.util.Calendar;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;

public enum DiaSemana {
	
	DOMINGO(Calendar.SUNDAY), 
	SEGUNDA_FEIRA(Calendar.MONDAY), 
	TERCA_FEIRA(Calendar.TUESDAY), 
	QUARTA_FEIRA(Calendar.WEDNESDAY),
	QUINTA_FEIRA(Calendar.THURSDAY), 
	SEXTA_FEIRA(Calendar.FRIDAY), 
	SABADO(Calendar.SATURDAY);
	
	private int codigoDiaSemana;
	
	private DiaSemana(int codigoDiaSemana){
		this.codigoDiaSemana = codigoDiaSemana;
	}

	public int getCodigoDiaSemana() {
		return codigoDiaSemana;
	}
	
	public static DiaSemana getByCodigoDiaSemana(Integer codigo){
		
		for (DiaSemana diaSemana : DiaSemana.values()){
			if (codigo.equals(diaSemana.getCodigoDiaSemana())){
				return diaSemana;
			}
		}
		
		return null;
	}
	
	public static DiaSemana getByDate(Date date) {

		return getByCodigoDiaSemana(DateUtil.obterDiaDaSemana(date));
	}
}
