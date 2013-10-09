package br.com.abril.nds.model.integracao;


public enum StatusIntegracao {
	LIBERADO("LIBERADO"),
	EM_PROCESSAMENTO("EM PROCESSO"),
	SOLICITADO("SOLICITADO"),
	REJEITADO("REJEITADO"),
	DESPREZADO("DESPREZADO"),
	NAO_INTEGRADO("NAO_INTEGRADO"),
	RE_INTEGRADO("RE_INTEGRADO"),
	INTEGRADO("INTEGRADO"),
	NAO_INTEGRAR("NAO_INTEGRAR"),
	AGUARDANDO_GFS("AGUARDANDO_GFS"),
	FORA_DO_PRAZO("FORA DO PRAZO"),
	FORA_DO_PRAZO_ENCALHE("FORA DO PRAZO - ENCALHE");
	
	private String descricao;
	
	private StatusIntegracao (String descricao){
		this.descricao = descricao;
	}
	
	public static StatusIntegracao obterPelaDescricao(String descricao) {
		
		for (StatusIntegracao status : StatusIntegracao.values()) {
			
			if (status.getDescricao().equals(descricao)) {
				
				return status;
			}
		}
		
		return null;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}
