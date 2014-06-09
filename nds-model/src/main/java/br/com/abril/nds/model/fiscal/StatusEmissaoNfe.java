package br.com.abril.nds.model.fiscal;

public enum StatusEmissaoNfe {

	AGUARDANDO_PROCESSAMENTO("Aguardando Processamento"),
	
	PROCESSAMENTO_REJEITADO("Processamento Rejeitado"),
	
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
