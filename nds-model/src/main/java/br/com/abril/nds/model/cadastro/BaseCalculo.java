package br.com.abril.nds.model.cadastro;

public enum BaseCalculo {

	FATURAMENTO_BRUTO("B"),
	FATURAMENTO_LIQUIDO("L");
	
	private String value;
	
	private BaseCalculo(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
