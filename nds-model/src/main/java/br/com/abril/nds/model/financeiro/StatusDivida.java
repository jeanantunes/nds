package br.com.abril.nds.model.financeiro;

public enum StatusDivida {
	
	EM_ABERTO("Em Aberto"), 
	QUITADA("Quitada"),
	NEGOCIADA("Negociada"),
	POSTERGADA("Postergada"),
	PENDENTE_INADIMPLENCIA("Pendente por inadimplência"),
	BOLETO_ANTECIPADO_EM_ABERTO("Boleto em branco");

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
