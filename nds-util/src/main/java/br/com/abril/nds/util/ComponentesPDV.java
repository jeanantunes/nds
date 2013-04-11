package br.com.abril.nds.util;

public enum ComponentesPDV {

	TIPO_PONTO_DE_VENDA("Tipo Ponto de Venda"),
	GERADOR_DE_FLUXO("Gerador de Fluxo"),
	BAIRRO("Bairro"),     
	REGIAO("Região"),       
	COTAS_A_VISTA("Cota À Vista"),
	COTAS_NOVAS_RETIVADAS("Cotas Novas/Reativadas"),
	AREA_DE_INFLUENCIA("Área de Influência"),
	DISTRITO("Distrito");   
	
	private String descricao;
	
	private ComponentesPDV(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}

	
}
