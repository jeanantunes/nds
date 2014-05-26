package br.com.abril.nds.enums;

/**
 * Enum que representa os tipos de Flags.
 * 
 */
public enum Flag {
	
	COTA_EXIGE_NF_E(Dominio.COTA, "Cota exige NF-e"),
	COTA_CONTRIBUINTE_ICMS(Dominio.COTA, "Cota Contrinbuinte de ICMS");
	
	private String descricao;
	
	private Dominio dominio;
	
	private Flag(Dominio dominio, String descricao) {

		this.descricao = descricao;
		this.dominio = dominio;
	}

	public String getDescricao() {
		return descricao;
	}

	public Dominio getDominio() {
		return dominio;
	}

	@Override
	public String toString() {

		return this.descricao;
	}
}