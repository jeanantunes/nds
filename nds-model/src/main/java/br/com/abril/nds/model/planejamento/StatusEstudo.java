package br.com.abril.nds.model.planejamento;

public enum StatusEstudo {
	
	GERADO("GERADO"),
	ANALISAR("ANALISAR"),
	LIBERADO("LIBERADO");
	
	/** Descrição do status. */
	private String descricao;
	
	/** Método construtor padrão. */
	private StatusEstudo(String descricao) {
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
