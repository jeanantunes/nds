package br.com.abril.nds.model.cadastro;

public enum ClasseSocial {
	
	CLASSE_AB("Classe AB"),
	CLASSE_A("Classe A"),
	CLASSE_C("Classe C"),
	CLASSE_BC("Classe BC"),
	CLASSE_D("Classe D");
	
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
