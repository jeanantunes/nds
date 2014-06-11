package br.com.abril.nds.model.fiscal;

public enum StatusEmissaoNfe {

	AGUARDANDO_PROCESSAMENTO("Aguardando Processamento"),
	
	EM_PROCESSAMENTO("Em Processamento"),
	
	PROCESSAMENTO_REJEITADO("Processamento Rejeitado"),
	
	AGUARDANDO_ACAO_DO_USUARIO("Aguardando Ação do Usuário"),
	
	NFE_AUTORIZADA("Nf-e Autorizada"),
	
	NFE_REJEITADA("Nf-e Rejeitada"),
	
	NFE_DENEGADA("Nf-e Denegada");
	
	private StatusEmissaoNfe(String descricao) {
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
	
	
}
