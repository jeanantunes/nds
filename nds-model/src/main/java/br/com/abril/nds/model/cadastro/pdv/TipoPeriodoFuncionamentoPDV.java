package br.com.abril.nds.model.cadastro.pdv;


/**
 * Enumeração com os tipos de período de funcionamento do PDV
 *  
 * @author francisco.garcia
 *
 */
public enum TipoPeriodoFuncionamentoPDV {

	DIARIA("Diária"), 
	SEGUNDA_SEXTA("Segunda à Sexta"), 
	FINAIS_SEMANA("Finais de Semana"), 
	FERIADOS("Feriados"), 
	VINTE_QUATRO_HORAS("24 horas"), 
	DOMINGO("Domingo"), 
	SEGUNDA_FEIRA("Segunda-Feira"), 
	TERCA_FEIRA("Terça-Feira"), 
	QUARTA_FEIRA("Quarta-Feira"),
	QUINTA_FEIRA("Quinta-Feira"), 
	SEXTA_FEIRA("Sexta-Feira"), 
	SABADO("Sábado");
	

	private String descricao;

	private TipoPeriodoFuncionamentoPDV(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	
}
