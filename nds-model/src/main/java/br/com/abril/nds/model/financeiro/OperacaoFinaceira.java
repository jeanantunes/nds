package br.com.abril.nds.model.financeiro;

public enum OperacaoFinaceira {
	
	CREDITO("Crédito","C"), 
	DEBITO("Débito","D");


	private String descricao;
	
	private String siglaOperacao;
	
	private OperacaoFinaceira(String descricao,String siglaOperacao) {

		this.descricao = descricao;
		this.siglaOperacao = siglaOperacao;
	}
	
	public String getSiglaOperacao(){
		return siglaOperacao;
	} 
	
	public void setSiglaOperacao(String siglaOperacao){
		this.siglaOperacao = siglaOperacao;
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