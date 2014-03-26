package br.com.abril.nds.model.cadastro;

public enum TipoFeriado {
	
	FEDERAL("Federal"),
	ESTADUAL("Estadual"),
	MUNICIPAL("Municipal");
	
	private String descricao;
	
	private TipoFeriado(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}

}
