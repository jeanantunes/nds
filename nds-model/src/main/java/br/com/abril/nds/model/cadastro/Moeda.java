package br.com.abril.nds.model.cadastro;

public enum Moeda {
	
	REAL(9);
	
	private Moeda(int codigo) {
		codigo = this.codigo;
	}
	
	private int codigo;
	
	public int getCodigo(int codigo) {
		return codigo;
	}

}
