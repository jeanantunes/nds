package br.com.abril.nds.enums;

/**
 * Enum que representa os tipos de Flags.
 * 
 */
public enum TipoFlag {
	
	COTA_EXIGE_NF_E("Cota exige NF-e"),
	COTA_CONTRIBUINTE_ICMS("Cota Contrinbuinte de ICMS");
	
	private String descricao;
	
	private TipoFlag(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {

		return this.descricao;
	}
}