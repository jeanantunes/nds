package br.com.abril.nds.model.cadastro;

public enum Periodicidade {

	DIARIO(1,"D", "diario"),
	SEMANAL(2,"S", "semanal"),
	QUINZENAL(3,"QN", "quinzenal"),
	MENSAL(4,"M", "mensal"),
	BIMESTRAL(5,"B", "bimestral"),
	TRIMESTRAL(6,"T", "trimestral"),
	QUADRIMESTRAL(7,"QD", "quadrimestral"),
	ANUAL(8,"A", "anual"),
	ESPORADICO(9,"E", "esporadico");

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