package br.com.abril.nds.model.fiscal;

public enum TipoEntidadeDestinoFlag {

	COTA("Cota"),
	DISTRIBUIDOR("Distribuidor");
	
	private String descricao;
	
	TipoEntidadeDestinoFlag(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}