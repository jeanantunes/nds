package br.com.abril.nds.model.cadastro;

public enum FormaFisica {
	
	REVISTA("Revista"),
	FASCICULO("Fasciculo"),
	LIVRO("Livro"),
	CROMO("Cromo"),
	CARDS("Cards"),
	ALBUM("Album"),
	GUIA("Guia"),
	CD_ROM("CD ROM"),
	POSTER("Poster"),
	JORNAL("Jornal"),
	CAPS("Caps"),
	CAPA_DURA("Capa Dura"),
	CLASSE_CD("Classe CD"),
	REVISTA_DIGITAL("Revista Digital"),
	DVD("DVD"),
	LIVRO_ILUSTRADO("Livro Ilustrado"),
	OUTROS("Outros");
	
	private String descricao;
	
	private FormaFisica(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescFormaFisica(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
