package br.com.abril.nds.model.cadastro;

public enum TipoImpressaoInterfaceLED {

	MODELO_1("Modelo 1"),
	MODELO_2("Modelo 2"),
	MODELO_3("Modelo 3"),
	MODELO_4("Modelo 4");
	
	private String descricao;
	
	private TipoImpressaoInterfaceLED(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoImpressaoNE() {
		return this.descricao;
	}
	
}
