package br.com.abril.nds.model.cadastro;

public enum TipoImpressaoNECADANFE {

	MODELO_1("Modelo 1"),
	MODELO_2("Modelo 2"),
	DANFE("Danfe");
	
	private String descricao;
	
	private TipoImpressaoNECADANFE(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoImpressaoNECADANFE() {
		return this.descricao;
	}
	
}
