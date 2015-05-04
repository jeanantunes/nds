package br.com.abril.nds.util;

/**
 * Enum que representa o tipos de baixa de cobrança.
 * 
 * @author Discover Technology
 */
public enum TipoBaixaCobranca {
	
	AUTOMATICA("Automática"),
	MANUAL("Manual"),
	CONSOLIDADA("Consolidada");
	
	private String descricao;
	
	private TipoBaixaCobranca(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}