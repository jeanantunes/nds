package br.com.abril.nds.enums;

public enum OpcoesFiltro {
	
	SIM("Sim"),
	NAO("Não"),
	TODOS("Todos"),
	AMBOS("Ambos");
	
	private String descricao;
	
	OpcoesFiltro(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}