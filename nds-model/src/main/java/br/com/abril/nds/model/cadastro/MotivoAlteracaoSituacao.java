package br.com.abril.nds.model.cadastro;

public enum MotivoAlteracaoSituacao {
	
	INADIMPLENCIA("Inadimplência"),
	REFORMA("Reforma"),
	VENDA_PDV("Venda do PDV"),
	DOENCA("Doença"),
	INCIDENTES("Incidentes"),
	RECESSO_ESTUDANTIL("Recesso Estudantil"),
	CHAMADAO("Chamadão"),
	OUTROS("Outros");

	private String descricao;
	
	private MotivoAlteracaoSituacao(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
	
}
