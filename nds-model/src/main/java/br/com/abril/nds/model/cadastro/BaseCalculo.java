package br.com.abril.nds.model.cadastro;

public enum BaseCalculo {

	FATURAMENTO_BRUTO("B", "Faturamento Bruto"),
	FATURAMENTO_LIQUIDO("L", "Faturamento Liquido");

	private String key;

	private String value;
	
	private BaseCalculo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	public String getKey() {
		return this.key;
	}
}
