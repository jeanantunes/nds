package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public enum EstadoCivil {
	SOLTEIRO("Solteiro"),
	CASADO("Casado"),
	DIVORCIADO("Divorciado"),
	VIUVO("Viúvo");
	
	private String descricao;

	private EstadoCivil(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}