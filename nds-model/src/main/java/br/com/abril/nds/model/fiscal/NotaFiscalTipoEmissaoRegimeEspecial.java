package br.com.abril.nds.model.fiscal;

public enum NotaFiscalTipoEmissaoRegimeEspecial {
	
	COTA_CONTRIBUINTE_EXIGE_NFE("Cota Contribuinte / Exige NF-e"),
	CONSOLIDADO("Consolidado"),
	AMBOS("Ambos");
	
	private String descricao;
	
	NotaFiscalTipoEmissaoRegimeEspecial(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}