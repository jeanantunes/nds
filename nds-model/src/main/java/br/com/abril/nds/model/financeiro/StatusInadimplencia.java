package br.com.abril.nds.model.financeiro;

public enum StatusInadimplencia {
	
	ATIVA("Em Aberto"),
	QUITADA("Paga");
	
	private String descricao;

	private StatusInadimplencia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
