package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public enum GrupoProduto {	
	
	REVISTA(1,"Revista"),
	FASCICULO(2,"Fasciculo"),
	LIVRO(3,"Livro"),
	CROMO(4,"Cromo"),
	CARDS(5,"Cards"),
	ALBUM(6,"Album"),
	GUIA(7,"Guia"),
	CD_ROM(12,"CD Rom"),
	POSTER(13,"Poster"),
	JORNAL(14,"Jornal"),
	CAPS(17,"Caps"),
	CAPA_DURA(18,"Capa Dura"),
	REVISTA_DIGITAL(19,"Revista Digital"),
	DVD(24,"Dvd"),
	LIVRO_ILUSTRADO(36,"Livro Ilustrado"),
	OUTROS(99,"Outros"),
	VALE_DESCONTO(8,"Vale Desconto"),
	CARTELA(9,"Cartela"),
	COLECIONAVEL(10,"Colecionável"),
	IMPRESSOS(99,"Impressos");
	
	private Integer codigo;
	
	private String nome;
	
	GrupoProduto(Integer codigo, String nome){
		
		this.codigo = codigo;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	/**
	 * @return the nome
	 */
	@Override
	public String toString() {
		return this.nome;
	}

}