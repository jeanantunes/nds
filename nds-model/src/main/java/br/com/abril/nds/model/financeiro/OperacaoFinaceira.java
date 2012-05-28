package br.com.abril.nds.model.financeiro;

public enum OperacaoFinaceira {
	
	CREDITO("Crédito"), 
	DEBITO("Débito");


	private String descricao;
	
	private OperacaoFinaceira(String descricao) {

		this.descricao = descricao;
	}

	@Override
	public String toString() {

		return this.descricao;
	}
}
