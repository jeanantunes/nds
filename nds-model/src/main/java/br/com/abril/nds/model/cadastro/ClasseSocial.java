package br.com.abril.nds.model.cadastro;

public enum ClasseSocial {
	
	CLASSE_A("Classe A"),
	CLASSE_AB("Classe AB"),
	CLASSE_ABC("Classe ABC"),
	CLASSE_B("Classe B"),
	CLASSE_BC("Classe BC"),
	CLASSE_BCD("Classe BCD"),
	CLASSE_C("Classe C"),
	CLASSE_CD("Classe CD"),
	CLASSE_CDE("Classe CDE"),
	CLASSE_D("Classe D"),
	CLASSE_E("Classe E");
	
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
