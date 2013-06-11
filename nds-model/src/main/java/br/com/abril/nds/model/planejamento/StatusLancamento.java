package br.com.abril.nds.model.planejamento;

public enum StatusLancamento {
	
	PLANEJADO("Planejado"),
	CONFIRMADO("Confirmado"),
	EM_BALANCEAMENTO("Em Balanceamento"),
	BALANCEADO("Balanceado"),
	ESTUDO_FECHADO("Estudo Fechado"),
	FURO("Furo"),
	EXPEDIDO("Expedido"),
	EM_BALANCEAMENTO_RECOLHIMENTO("Em Balanceamento Recolhimento"),
	//EM_BALANCEAMENTO_LANCAMENTO("Em Balanceamento Lancamento"),
	BALANCEADO_RECOLHIMENTO("Balanceado Recolhimento"),
	EXCLUIDO_RECOLHIMENTO("Excluído Recolhimento"),
	EM_RECOLHIMENTO("Em Recolhimento"),
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
