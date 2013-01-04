package br.com.abril.nds.model.planejamento.fornecedor;

public enum StatusCeNDS {

	ABERTO("Aberto"),
	FECHADO("Fechado");

	private String descricao;
	
	private StatusCeNDS(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
