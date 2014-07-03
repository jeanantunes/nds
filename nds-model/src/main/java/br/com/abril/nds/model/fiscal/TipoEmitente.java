package br.com.abril.nds.model.fiscal;

public enum TipoEmitente {

	COTA("Cota"),
	DISTRIBUIDOR("Distribuidor"),
	FORNECEDOR("Fornecedor");
	
	private String descricao;
	
	TipoEmitente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}