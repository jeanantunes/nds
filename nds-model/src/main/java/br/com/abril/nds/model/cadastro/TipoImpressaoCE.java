package br.com.abril.nds.model.cadastro;

public enum TipoImpressaoCE {

	MODELO_1("Modelo 1"),
	MODELO_2("Modelo 2");
	
	private String descricao;
	
	private TipoImpressaoCE(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoImpressaoCE() {
		return this.descricao;
	}
	
}
