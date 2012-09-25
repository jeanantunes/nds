package br.com.abril.nds.model.estoque;

public enum TipoEstoque {

	LANCAMENTO("Lançamento"),
	LANCAMENTO_JURAMENTADO("Lançamento Juramentado"),
	SUPLEMENTAR("Suplementar"),
	RECOLHIMENTO("Recolhimento"),
	PRODUTOS_DANIFICADOS("Produtos Danificados"),
	DEVOLUCAO_ENCALHE("Devolução Encalhe"),
	DEVOLUCAO_FORNECEDOR("Devolução Fornecedor"), 
	JURAMENTADO("Juramentado"), 
	DANIFICADO("Danificado");
	
	String descricao;
	
	private TipoEstoque(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
