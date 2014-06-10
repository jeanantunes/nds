package br.com.abril.nds.model.fiscal;

import br.com.abril.nds.model.fiscal.nota.NotaFiscalEnum;


/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoOperacao implements NotaFiscalEnum {
	ENTRADA("Entrada","E"),
	SAIDA("Saída","S");
	
	
	TipoOperacao(String descricao, String simpleValue) {
		this.descricao = descricao;
		this.simpleValue =simpleValue;
	}
	
	private String descricao;
	private String simpleValue;

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atribuí descricao
	 * @param descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the simpleValue
	 */
	public String getSimpleValue() {
		return simpleValue;
	}

	/**
	 * @param simpleValue the simpleValue to set
	 */
	public void setSimpleValue(String simpleValue) {
		this.simpleValue = simpleValue;
	}
	
	@Override
	public Integer getIntValue() {
		return this.ordinal();
	}
}