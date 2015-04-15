package br.com.abril.nds.model.fiscal;

public enum TipoDestinatario {

	COTA("Cota"),
	DISTRIBUIDOR("Distribuidor"),
	FORNECEDOR("Fornecedor");
	
	private String descricao;
	
	TipoDestinatario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}