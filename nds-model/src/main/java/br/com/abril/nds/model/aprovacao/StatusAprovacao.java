package br.com.abril.nds.model.aprovacao;

public enum StatusAprovacao {
	
	PENDENTE("P"), 
	APROVADO("A"), 
	REJEITADO("R");

	private String descricao;
	
	private StatusAprovacao(String descricao) {
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
