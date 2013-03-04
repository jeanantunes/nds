package br.com.abril.nds.util;

public enum ComponentesPDV {

	TipoPontodeVenda("Tipo Ponto de Venda"),
	GeradorDeFluxo("Gerador de Fluxo"),
	Bairro("Bairro"),     
	Região("Região"),       
	CotasAVista("Cota À Vista"),
	CotasNovasRetivadas("Cotas Novas/Reativadas"),
	Area_de_Influência("Área de Influência"),
	Distrito("Distrito");   
	
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
	
}

