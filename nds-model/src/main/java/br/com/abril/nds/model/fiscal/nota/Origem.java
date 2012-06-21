package br.com.abril.nds.model.fiscal.nota;

/**
 * Modalidade de determinação da BC do ICMS
 * 
 * @author Diego Fernandes
 * 
 */
public enum Origem {
	/**
	 * 0 – Nacional
	 */
	NACIONAL,
	/**
	 * 1 – Estrangeira – Importação direta
	 */
	ESTRANGEIRA_IMPORTACAO,
	/**
	 * 2 – Estrangeira – Adquirida no mercado interno
	 */
	ESTRANGEIRA_MERCADO;
}