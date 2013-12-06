package br.com.abril.nds.model;

import java.util.Calendar;
import java.util.Date;

import br.com.abril.nds.util.SemanaUtil;

public enum DiaSemana {
	
	DOMINGO(Calendar.SUNDAY,"Domingo"), 
	SEGUNDA_FEIRA(Calendar.MONDAY,"Segunda"), 
	TERCA_FEIRA(Calendar.TUESDAY,"Terça"), 
	QUARTA_FEIRA(Calendar.WEDNESDAY,"Quarta"),
	QUINTA_FEIRA(Calendar.THURSDAY,"Quinta"), 
	SEXTA_FEIRA(Calendar.FRIDAY,"Sexta"), 
	SABADO(Calendar.SATURDAY,"Sábado");
	
	private int codigoDiaSemana;
	private String descricaDiaSemana;
	
	private DiaSemana(int codigoDiaSemana,String descricaDiaSemana){
		this.codigoDiaSemana = codigoDiaSemana;
		this.descricaDiaSemana = descricaDiaSemana;
	}

	public int getCodigoDiaSemana() {
		return codigoDiaSemana;
	}
	
	public String getDescricaoDiaSemana(){
		return descricaDiaSemana;
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

		return getByCodigoDiaSemana(SemanaUtil.obterDiaDaSemana(date));
	}
	
}