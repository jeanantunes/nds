package br.com.abril.nds.model.cadastro;

public enum CanalDistribuicao {

	BANCAS("Bancas"),
	VAREJO("Varejo");
	
	private String descricao;
	
	private CanalDistribuicao(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return descricao;
	}
}
