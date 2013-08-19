package br.com.abril.nds.model;

public enum StatusOperacional {

	OPERANDO("Operando"),
	ENCERRADO("Encerrado"),
	FECHAMENTO("Fechamento"),
	OFFLINE("Offline");

	/** Descrição do status. */
	private String descricao;
	
	/** Método construtor padrão. */
	private StatusOperacional(String descricao) {
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
