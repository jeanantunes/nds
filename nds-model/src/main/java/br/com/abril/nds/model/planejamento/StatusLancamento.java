package br.com.abril.nds.model.planejamento;

public enum StatusLancamento {
	
	PLANEJADO("Planejado"),
	CONFIRMADO("Confirmado"),
	EM_BALANCEAMENTO("Em Balanceamento"),
	BALANCEADO("Balanceado"),
	ESTUDO_FECHADO("Estudo Fechado"),
	FURO("Furo"),
	EXPEDIDO("Expedido"), // Edições abertas
	EM_BALANCEAMENTO_RECOLHIMENTO("Em Balanceamento Recolhimento"), // Edições abertas
	BALANCEADO_RECOLHIMENTO("Balanceado Recolhimento"), // Edições abertas
	EXCLUIDO_RECOLHIMENTO("Excluído Recolhimento"), // Edições abertas
	RECOLHIDO("Recolhido"),
	CANCELADO("Cancelado"),
	FECHADO("Fechado");
	
	/** Descrição do status. */
	private String descricao;
	
	/** Método construtor padrão. */
	private StatusLancamento(String descricao) {
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
