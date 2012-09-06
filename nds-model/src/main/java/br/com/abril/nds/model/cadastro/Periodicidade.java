package br.com.abril.nds.model.cadastro;

public enum Periodicidade {

	DIARIO("D", "diario"),
	SEMANAL("S", "semanal"),
	MENSAL("M", "mensal");
	
	private String value;
	
	private String descricao;
	
	private Periodicidade(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}
	
	public String getValue() {
		return this.value;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}