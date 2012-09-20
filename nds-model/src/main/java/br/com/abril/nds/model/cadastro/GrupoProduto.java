package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public enum GrupoProduto {	
	
	CROMO("Cromo"),
	REVISTA("Revista"),
	LIVRO("Livro"),
	JORNAL("Jornal"),
	VALE_DESCONTO("Vale Desconto"),
	CARTELA("Cartela"),
	COLECIONAVEL("Colecionável"),
	OUTROS("Outros"),
	GUIA("Guia"),
	ALBUM("Album");
	
	private Integer codigo;
	private String nome;
	
	GrupoProduto(String nome){
		this.setNome(nome);
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @return the nome
	 */
	@Override
	public String toString() {
		return this.nome;
	}

}