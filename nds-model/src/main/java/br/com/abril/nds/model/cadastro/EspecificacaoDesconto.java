package br.com.abril.nds.model.cadastro;

public enum EspecificacaoDesconto {
	
	GERAL("Geral"),
	ESPECIFICO("Específico"),
	PRODUTO("Produto");
	
	private String descricao;
	
	private EspecificacaoDesconto(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescDesconto(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

}
