package br.com.abril.nds.model.cadastro;

public enum SituacaoTributaria {

	ISENTO(1, "Produto Nacional Isento ICMS/IPI"),
	IMUNE(2, "Produto Nacional Imune ICMS/IPI"),
	OUTROS(3, "Outros");
	
	private Integer key;
	private String value;
	
	private SituacaoTributaria(Integer key, String value) {
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
