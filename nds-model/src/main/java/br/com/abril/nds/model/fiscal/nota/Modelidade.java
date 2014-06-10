package br.com.abril.nds.model.fiscal.nota;

public enum Modelidade {
	/**
	 * 0 - Margem Valor Agregado (%)
	 */
	MARGEM,
	/**
	 * 1 - Pauta (Valor)
	 */
	PAUTA,
	/**
	 * 2 - Preço Tabelado Máx. (Valor)
	 */
	PRECO_TABELADO,
	/**
	 * 3 - valor da operação
	 */
	VALOR_OPERACAO;

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(this.ordinal());
	}
}