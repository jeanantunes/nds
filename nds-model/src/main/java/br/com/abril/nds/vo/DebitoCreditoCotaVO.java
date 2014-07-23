package br.com.abril.nds.vo;


public class DebitoCreditoCotaVO {

	private String tipoLancamento;
	private String valor;
	private String dataLancamento;
	private String observacoes;

	/**
	 * @return the tipoLancamento
	 */
	public String getTipoLancamento() {
		return tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento
	 *            the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the observacoes
	 */
	public String getObservacoes() {
		return observacoes;
	}

	/**
	 * @param observacoes
	 *            the observacoes to set
	 */
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

}
