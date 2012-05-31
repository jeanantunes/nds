package br.com.abril.nds.model.cadastro;

public enum TributacaoFiscal {

	TRIBUTADO(1, "Tributado"),
	ISENTO(2, "Isento"),
	OUTROS(3, "Outros");

	private Integer key;
	private String value;
	
	private TributacaoFiscal(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
}
