package br.com.abril.nds.model.cadastro;

public enum TipoImpressaoNE {

	MODELO_1("Modelo 1"),
	MODELO_2("Modelo 2");
	
	private String descricao;
	
	private TipoImpressaoNE(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoImpressaoNE() {
		return this.descricao;
	}
	
}
