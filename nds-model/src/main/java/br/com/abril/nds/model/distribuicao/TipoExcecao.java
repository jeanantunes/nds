package br.com.abril.nds.model.distribuicao;

public enum TipoExcecao {
		
	SEGMENTO("Segmento"),
	PARCIAL("Parcial");
	
	private String descricao;
	
	private TipoExcecao(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
}
