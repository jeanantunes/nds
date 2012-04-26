package br.com.abril.nds.model.cadastro;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeração com os tipos de período de funcionamento do PDV
 *  
 * @author francisco.garcia
 *
 */
public enum TipoPeriodoFuncionamentoPDV {

	DIARIA("Diária"), 
	SEGUNDA_SEXTA("Segunda-Feira"), 
	FINAIS_SEMANA("Finais de Semana"), 
	FERIADOS("Feriados"), 
	VINTE_QUATRO_HORAS("24 horas"), 
	DOMINGO("Domingo"), 
	SEGUNDA_FEIRA("Segunda-Feira"), 
	TERCA_FEIRA("Terça-Feira"), 
	QUARTA_FEIRA("Quarta-Feira"),
	QUINTA_FEIRA("Quinta-Feira"), 
	SEXTA_FEIRA("Sexta-Deira"), 
	SABADO("Sábado");
	

	private String descricao;

	private TipoPeriodoFuncionamentoPDV(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	/**
	 * Obtém lista com os possíveis peridos a serem selecionados
	 * 
	 * @param selecionados - Periodos já selecionados
	 * @return - períodos que ainda podem ser selecionados
	 */
	public List<TipoPeriodoFuncionamentoPDV> getPeriodosPossiveis(List<TipoPeriodoFuncionamentoPDV> selecionados) {
		
		List<TipoPeriodoFuncionamentoPDV> possiveis = new ArrayList<TipoPeriodoFuncionamentoPDV>();
		
		for(TipoPeriodoFuncionamentoPDV periodo: TipoPeriodoFuncionamentoPDV.values()) {
			
			try{
				selecionados.add(periodo);
				validarPeriodos(selecionados);
				selecionados.remove(periodo);
				
				possiveis.add(periodo);
			} catch (Exception e) {
				selecionados.remove(periodo);
			}
		}
		return possiveis;
	}
	
	/**
	 * Valida se uma lista de períodos é valida, de acordo com as regras definidas na EMS 0159
	 * 
	 * @param periodos
	 * @throws Exception
	 */
	public void validarPeriodos(List<TipoPeriodoFuncionamentoPDV> periodos) throws Exception {
		
		validarDuplicidadeDePeriodo(periodos);
		
		if (periodos.contains(DIARIA)) {
			
			if(periodos.size()>1) {
				
				throw new Exception("Ao selecionar " + DIARIA.descricao + ", nenhum outro item deve ser incluido.");
			} 
		
		} else if (periodos.contains(VINTE_QUATRO_HORAS)) {
			
			if(periodos.size() > 1) {
				
				throw new Exception("Ao selecionar " + VINTE_QUATRO_HORAS.descricao + ", nenhum outro item deve ser incluido.");
			} 
		
		} else if (periodos.contains(SEGUNDA_SEXTA)) {
			
			if (periodos.contains(SEGUNDA_FEIRA)
				|| periodos.contains(TERCA_FEIRA)
				|| periodos.contains(QUARTA_FEIRA)
				|| periodos.contains(QUINTA_FEIRA)
				|| periodos.contains(SEXTA_FEIRA)) {
				
				throw new Exception("Ao selecionar o período de '"+SEGUNDA_SEXTA.descricao+"', não é permitido a selecao específica de um dia da semana.");				
			}
		} else if (periodos.contains(FINAIS_SEMANA)) {
			
			if (periodos.contains(SABADO)
				|| periodos.contains(DOMINGO)) {
				
				throw new Exception("Ao selecionar o período de '"+FINAIS_SEMANA.descricao+"', não é permitido a definição específíca para sábado ou domingo.");				
			}
		}		
	}

	/**
	 * Valida duplicidade de período
	 * 
	 * @param periodos - periodos
	 * @throws Exception - Exceção ao encontrar registro duplicado.
	 */
	private void validarDuplicidadeDePeriodo(List<TipoPeriodoFuncionamentoPDV> periodos) throws Exception {
		
		List<TipoPeriodoFuncionamentoPDV> clone = new ArrayList<TipoPeriodoFuncionamentoPDV>(periodos);
		
		for(TipoPeriodoFuncionamentoPDV item : periodos) {
			clone.remove(item);
		}
		
		if(clone.size() > 0) {
			throw new Exception("O período " + clone.get(0).descricao + " foi incluido a lista mais de uma vez.");
		}
	}
}
