package br.com.abril.nds.server.model;

public enum GrupoIndicador {

	LANCAMENTO("Lançamento", 1),
	RECOLHIMENTO("Recolhimento", 2),
	FINANCEIRO("Financeiro", 3),
	CONSIGNADO("Consignado", 4),
	JORNALEIRO("Jornaleiro", 5),
	QUALIDADE_OPERACIONAL("Qualidade Operacional", 6);
	
	private String descricao;
	
	private int ordemExibicao;
	
	private GrupoIndicador(String descricao, int ordemExibicao){
		
		this.descricao = descricao;
		this.ordemExibicao = ordemExibicao;
	}
	
	public String toString(){
		
		return this.descricao;
	}
	
	public int getOrdemExibicao(){
		
		return this.ordemExibicao;
	}

	public String getDescricao() {
		return descricao;
	}
}