package br.com.abril.nds.model.cadastro;

public enum FaixaEtaria {
	
	QUINZE_A_DEZENOVE("De 15 à 19 anos"),
	VINTE_A_VINTE_E_NOVE("De 20 à 29 anos"),
	TRINTA_A_QUARENTA_E_NOVE("De 30 à 49 anos"),
	MAIS_DE_CINQUENTA("50 anos ou mais");

	private String descricao;
	
	private FaixaEtaria(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescFaixaEtaria(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
