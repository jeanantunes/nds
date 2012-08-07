package br.com.abril.nds.model.cadastro;

public enum TemaProduto {

	SEMANAIS_DE_INFORMACAO("Semanais de Informação");
	
	private String descricao;
	
	private TemaProduto(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTemaProduto(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}
