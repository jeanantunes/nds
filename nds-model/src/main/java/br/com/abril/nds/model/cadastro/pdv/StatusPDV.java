package br.com.abril.nds.model.cadastro.pdv;

/**
 * Entidade para status de cadastro
 * do PDV
 * @author francisco.garcia
 *
 */
public enum StatusPDV {
	
	ATIVO("Ativo"), 
	
	SUSPENSO("Suspenso");
	
	private String descricao;

	private StatusPDV(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
