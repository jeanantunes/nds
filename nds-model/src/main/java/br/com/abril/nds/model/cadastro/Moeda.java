package br.com.abril.nds.model.cadastro;

public enum Moeda {
	
	REAL(9,"Real");
	
	private int codigo;
	private String descricao;
	
	private Moeda(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
	public int getCodigo() {
		return this.codigo;
	}

}
