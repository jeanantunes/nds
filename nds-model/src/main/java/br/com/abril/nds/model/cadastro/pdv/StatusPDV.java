package br.com.abril.nds.model.cadastro.pdv;

/**
 * Entidade para status de cadastro
 * do PDV
 * @author francisco.garcia
 *
 */
public enum StatusPDV {
	
	ATIVO("Ativo", "ATV"),
	
	SUSPENSO("Suspenso", "SUS");
	
	private String descricao;
	private String descricaoIcd;

	StatusPDV(String descricao, String descricaoIcd) {
		this.descricao = descricao;
		this.descricaoIcd = descricaoIcd;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoIcd() {
		return descricaoIcd;
	}
}
