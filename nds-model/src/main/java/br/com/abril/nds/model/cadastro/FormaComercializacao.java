package br.com.abril.nds.model.cadastro;

public enum FormaComercializacao {

	CONSIGNADO(1, "Consignado"),
	CONTA_FIRME(2, "Conta Firme");
	
	private Integer key;
	
	private String value;
	
	private FormaComercializacao(Integer key, String value) {
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
		return this.value;
	}
	
	
	@Override
	public String toString() {
		return this.value;
	}
}
