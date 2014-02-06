package br.com.abril.nds.model.integracao;


public enum StatusIntegracaoNFE {
	
	EM_PROCESSAMENTO("EM_PROCESSAMENTO"),
	APROVADO("APROVADO"),
	NAO_APROVADO("NAO_APROVADO");
	
	private String descricao;
	
	private StatusIntegracaoNFE (String descricao){
		
		this.descricao = descricao;
	}
	
	public static StatusIntegracaoNFE obterPelaDescricao(String descricao) {
		
		for (StatusIntegracaoNFE status : StatusIntegracaoNFE.values()) {
			
			if (status.getDescricao().equals(descricao)) {
				
				return status;
			}
		}
		
		return null;
	}
	
	public String getDescricao(){
		
		return this.descricao;
	}
	
	public static boolean isAprovado(StatusIntegracaoNFE status){
		return APROVADO.equals(status);
	}
}
