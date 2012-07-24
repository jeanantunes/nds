package br.com.abril.nds.server.model;

public enum GrupoIndicador {

	LANCAMENTO("Lançamento", 1),
	RECOLHIMENTO("Recolhimento", 2),
	FINANCEIRO("Financeiro", 3),
	CONSIGNADO("Consignado", 4),
	JORNALEIRO("Jornaleiro", 5),
	QUALIDADE_OPERACIONAL("Qualidade Operacional", 6);
	
	private String descricao;
	
	private int ordemExibicaoGrupo;
	
	private GrupoIndicador(String descricao, int ordemExibicaoGrupo){
		
		this.descricao = descricao;
		this.ordemExibicaoGrupo = ordemExibicaoGrupo;
	}
	
	public int getOrdemExibicaoGrupo(){
		
		return this.ordemExibicaoGrupo;
	}

	public String getDescricao() {
		return descricao;
	}
}