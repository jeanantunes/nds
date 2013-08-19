package br.com.abril.nds.model.cadastro;

public enum FormaEmissao {
	
	
	INDIVIDUAL_AGREGADA("Individual - Agregada a C.E."), 
	INDIVIDUAL_BOX("Individual - No box de Encalhe"), 
	EM_MASSA("Em massa - Após geração de dívida"), 
	NAO_IMPRIME("Não imprime"),
	PONTUAL("Pontual, na Integração CE Fornecedor");

	
	private String descricao;
	
	private FormaEmissao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescFormaEmissao(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}