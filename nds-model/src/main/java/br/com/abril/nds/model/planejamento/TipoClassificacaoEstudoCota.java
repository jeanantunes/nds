package br.com.abril.nds.model.planejamento;

public enum TipoClassificacaoEstudoCota {
	
	FX("FX"),
	MM("MM");
	
	/** Descrição do status. */
	private String descricao;
	
	/** Método construtor padrão. */
	private TipoClassificacaoEstudoCota(String descricao) {
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
