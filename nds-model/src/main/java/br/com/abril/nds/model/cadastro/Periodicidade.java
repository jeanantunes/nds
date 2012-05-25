package br.com.abril.nds.model.cadastro;

public enum Periodicidade {

	DIARIO("D"),
	SEMANAL("S"),
	MENSAL("M");
	
	private String value;
	
	private Periodicidade(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
