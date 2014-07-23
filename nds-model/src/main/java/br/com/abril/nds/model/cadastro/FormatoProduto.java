package br.com.abril.nds.model.cadastro;

public enum FormatoProduto {

	AMERICANO("Americano"),
	CARAS("Caras"),
	OUTROS("Outros"),
	PATO("Pato"),
	VEJA("Veja");
	
	private String descricao;
	
	private FormatoProduto(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescFormatoProduto(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}
