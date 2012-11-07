package br.com.abril.nds.model.cadastro;

public enum Periodicidade {

	DIARIO(1,"D", "Diário"),
	SEMANAL(2,"S", "Semanal"),
	QUINZENAL(3,"QN", "Quinzenal"),
	MENSAL(4,"M", "Mensal"),
	BIMESTRAL(5,"B", "Bimestral"),
	TRIMESTRAL(6,"T", "Trimestral"),
	QUADRIMESTRAL(7,"QD", "Quadrimestral"),
	ANUAL(8,"A", "Anual"),
	ESPORADICO(9,"E", "Esporádico");

	private Integer codigo;
	
	private String value;
	
	private String descricao;
	
	private Periodicidade(Integer codigo, String value, String descricao) {
		
		this.codigo = codigo;
		this.value = value;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getValue() {
		return this.value;
	}

	public String getDescricao() {
		return descricao;
	}
}