package br.com.abril.nds.model.cadastro;

public enum PeriodicidadeCobranca{
	
	DIARIO("D", "Diário"),
	SEMANAL("S", "Semanal"),
	QUINZENAL("Q", "Semanal"),
	MENSAL("M", "Mensal");
	
	private String codigo;
	
	private String descricao;
	
	private PeriodicidadeCobranca(String codigo, String descricao){
		
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
