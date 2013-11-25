<<<<<<< HEAD
package br.com.abril.nds.model.integracao;


public enum StatusIntegracao {
	LIBERADO("LIBERADO"),
	EM_PROCESSAMENTO("EM PROCESSAMENTO"),
	EM_PROCESSO("EM PROCESSO"),
	SOLICITADO("SOLICITADO"),
	REJEITADO("REJEITADO"),
	DESPREZADO("DESPREZADO"),
	NAO_INTEGRADO("NÃO INTEGRADO"),
	RE_INTEGRADO("RE INTEGRADO"),
	INTEGRADO("INTEGRADO"),
	NAO_INTEGRAR("NÃO INTEGRAR"),
	AGUARDANDO_GFS("AGUARDANDO GFS"),
	FORA_DO_PRAZO("FORA DO PRAZO"),
	ENCALHE("ENCALHE");
	
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
=======
package br.com.abril.nds.model.integracao;

public enum StatusIntegracao {
	LIBERADO,
	EM_PROCESSO,
	SOLICITADO,
	REJEITADO,
	DESPREZADO,
	NAO_INTEGRADO
}
>>>>>>> fase2
