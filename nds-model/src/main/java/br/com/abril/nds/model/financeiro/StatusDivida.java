<<<<<<< HEAD
package br.com.abril.nds.model.financeiro;

public enum StatusDivida {
	
	EM_ABERTO("Em Aberto"), 
	QUITADA("Quitada"),
	NEGOCIADA("Negociada"),
	POSTERGADA("Postergada"),
	PENDENTE("Pendente"),
	PENDENTE_INADIMPLENCIA("Pendente por inadimplência");

	private String descricao;
	
	private StatusDivida(String descricao) {
		
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}

}
=======
package br.com.abril.nds.model.financeiro;

public enum StatusDivida {
	
	EM_ABERTO("Em Aberto"), 
	QUITADA("Quitada"),
	NEGOCIADA("Negociada"),
	POSTERGADA("Postergada");

	private String descricao;
	
	private StatusDivida(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}

}
>>>>>>> fase2
