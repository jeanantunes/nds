package br.com.abril.nds.model.cadastro;

public enum FormatoProduto {

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
