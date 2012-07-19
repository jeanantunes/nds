package br.com.abril.nds.model.cadastro;

public enum TributacaoFiscal {

	TRIBUTADO(1, "Tributado", "00"),
	ISENTO(2, "Isento", "40"),
	OUTROS(3, "Outros", "90");

	private Integer key;
	private String value;
	private String cst;
	
	private TributacaoFiscal(Integer key, String value, String cst) {
		this.key = key;
		this.value = value;
		this.cst = cst;
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
	
	public String getCST() {
		return this.cst;
	}
	
}
