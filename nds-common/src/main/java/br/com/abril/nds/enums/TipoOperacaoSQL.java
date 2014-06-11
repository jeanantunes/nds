package br.com.abril.nds.enums;

public enum TipoOperacaoSQL {
	
	DELETE("DELETE"),
	UPDATE("UPDATE"),
	INSERT("INSERT");
	
	private String operacao;
	
	private TipoOperacaoSQL(String operacao) {

		this.operacao = operacao;
	}

	/**
	 * @return the operacao
	 */
	public String getOperacao() {
		return operacao;
	}
}

