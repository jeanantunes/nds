package br.com.abril.nds.model.cadastro;

public enum ClasseSocial {
	
	CLASSE_AB("Classe AB"),
	CLASSE_A("Classe A"),
	CLASSE_CD("Classe CD"),
	CLASSE_BC("Classe BC"),
	CLASSE_DE("Classe DE");
	
	private String descricao;
	
	private ClasseSocial(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescClasseSocial(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
