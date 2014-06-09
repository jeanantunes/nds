package br.com.abril.nds.model.cadastro;


public enum TipoRoteiro {
	
	NORMAL("Normal"),
	ESPECIAL("Especial");
	
	private String descricao;
	
	private TipoRoteiro(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
	
}