package br.com.abril.nds.model.planejamento;

/**
 * Estados possíveis de um lancamento parcial
 * 
 * @author francisco.garcia
 *
 */
public enum StatusLancamentoParcial {
		
	PROJETADO("Projetado"), 
	RECOLHIDO("Recolhido"), 
	CANCELADO("Cancelado");
	
	private String descricao;
	
	private StatusLancamentoParcial(String descricao) {
		this.descricao = descricao;
	}
	
	public String toString() {
		return descricao;
	}
}
