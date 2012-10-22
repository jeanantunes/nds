package br.com.abril.nds.util;

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

