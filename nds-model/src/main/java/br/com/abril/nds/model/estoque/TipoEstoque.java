package br.com.abril.nds.model.estoque;

public enum TipoEstoque {

	LANCAMENTO("Lançamento", null),
	LANCAMENTO_JURAMENTADO("Lançamento Juramentado", null),
	SUPLEMENTAR("Suplementar", null),
	RECOLHIMENTO("Recolhimento", null),
	PRODUTOS_DANIFICADOS("Produtos Danificados", null),
	DEVOLUCAO_ENCALHE("Devolução Encalhe", "Encalhe"),
	DEVOLUCAO_FORNECEDOR("Devolução Fornecedor", null), 
	JURAMENTADO("Juramentado", null), 
	DANIFICADO("Danificado", null),
	PERDA("Perda", null),
	GANHO("Ganho", null),
	MATERIAL_PROMOCIONAL("Material Promocional", null),
	COTA("Cota", null);
	
	String descricao;
	
	String descricaoAbreviada;
	
	private TipoEstoque(String descricao, String descricaoAbreviada) {
		this.descricao = descricao;
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoAbreviada() {
		
		if(descricaoAbreviada == null || descricaoAbreviada.isEmpty()){
			return descricao;
		}
		
		return descricaoAbreviada;
	}
	
	
}
