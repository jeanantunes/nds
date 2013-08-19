package br.com.abril.nds.model.fiscal;

public enum StatusNFe {

	AGUARDANDO_PROCESSAMENTO("Aguardando Processamento"),
	EM_PROCESSAMENTO("Em Processamento"),
	PROCESSAMENTO_REJEITADO("Processamento Rejeitado"),
	AGUARDANDO_ACAO_DO_USUARIO("Aguardando Ação do Usuário"),
	NFE_AUTORIZADA("Nf-e Autorizada"),
	NFE_REJEITADA("Nf-e Rejeitada"),
	NFE_DENEGADA("Nf-e Denegada");
	
	StatusNFe(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atribuí descricao
	 * @param descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
