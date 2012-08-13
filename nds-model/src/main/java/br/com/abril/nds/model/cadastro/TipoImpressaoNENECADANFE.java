package br.com.abril.nds.model.cadastro;

public enum TipoImpressaoNENECADANFE {

	MODELO_1("Modelo 1"),
	MODELO_2("Modelo 2"),
	DANFE("Danfe");
	
	private String descricao;
	
	private TipoImpressaoNENECADANFE(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoImpressaoNENECADANFE() {
		return this.descricao;
	}
	
}
