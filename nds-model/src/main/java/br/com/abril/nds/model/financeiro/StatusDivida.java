package br.com.abril.nds.model.financeiro;

public enum StatusDivida {
	
	EM_ABERTO("Em Aberto"), 
	QUITADA("Quitada"),
	NEGOCIADA("Negociada"),
	POSTERGADA("Postergada"),
	PENDENTE("Pendente");

	private String descricao;
	
	private StatusDivida(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}

}
