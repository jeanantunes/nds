package br.com.abril.nds.server.model;

public enum TipoIndicador {

	TITULOS_LANCADOS("Títulos Lançados no Dia", 0),
	TITULOS_FURADOS("Títulos Furados no Dia", 1),
	CONSIGNADO("Consignado no Dia", 2),
	TITULOS_RECOLHIDOS("Títulos Recolhidos no Dia", 0),
	RECOLHIMENTO("Recolhimento no Dia", 1),
	VENCIMENTOS("VencImentos no Dia", 0),
	INADIMPLENCIA("Inadimplência no Dia", 1),
	INADIMPLENCIA_ACUMULADA("Inadimplência Acumulada", 2),
	COBRANCA("Cobrança no Dia", 3),
	COBRANCA_POSTERGADA("Cobrança no Dia Postergada", 4),
	TOTAL_RUA("Total na Rua", 0),
	TOTAL_RUA_INADIMPLENCIA("Total na Rua Inadimplente", 1),
	JORNALEIROS("Total", 0),
	JORNALEIROS_ATIVOS("Ativos", 1),
	JORNALEIROS_SUSPENSOS("Suspensos", 2),
	JORNALEIROS_INATIVOS("Inativos", 3),
	SOBRAS_DE("Sobras De", 0),
	SOBRAS_EM("Sobras Em", 1),
	FALTAS_DE("Faltas De", 2),
	FALTAS_EM("Faltas Em", 3);
	
	private String descricao;
	
	private int ordemExibicao;
	
	private TipoIndicador(String descricao, int ordemExibicao){
		
		this.descricao = descricao;
		this.ordemExibicao = ordemExibicao;
	}
	
	public int getOrdemExibicao(){
		
		return this.ordemExibicao;
	}

	public String getDescricao() {
		return descricao;
	}
}