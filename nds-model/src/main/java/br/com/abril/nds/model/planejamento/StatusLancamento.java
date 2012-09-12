package br.com.abril.nds.model.planejamento;

public enum StatusLancamento {
	
	PLANEJADO("Planejado"),
	CONFIRMADO("Confirmado"),
	BALANCEADO("Balanceado"),
	BALANCEADO_LANCAMENTO("Balanceado Lançamento"),
	ESTUDO_FECHADO("Estudo Fechado"),
	FURO("Furo"),
	EXPEDIDO("Expedido"),
	EM_BALANCEAMENTO_RECOLHIMENTO("Em Balanceamento Recolhimento"),
	BALANCEADO_RECOLHIMENTO("Balanceado Recolhimento"),
	RECOLHIDO("Recolhido"),
	CANCELADO("Cancelado"),

	// EMS 217:
	TRANSMITIDO("Transmitido"),
	PREVISTO("Previsto"),
	CALCULO_SOLICITADO("Cálculo Solicitado"),
	CALCULADO("Calculado"),
	EMITIDO("Emitido"),
	LIBERAR_CALCULO("Liberar Cálculo"),
	LANCADO("Lançado"),
	EM_RECOLHIMENTO("Em Recolhimento"),
	FECHADO("Fechado"),
	
	EXCLUIDO("Excluído");
	
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
