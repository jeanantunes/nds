package br.com.abril.nds.model.financeiro;

public enum OperacaoFinaceira {
	
	CREDITO("Crédito"), 
	DEBITO("Débito");


	private String descricao;
	
	private OperacaoFinaceira(String descricao) {

		this.descricao = descricao;
	}	

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
