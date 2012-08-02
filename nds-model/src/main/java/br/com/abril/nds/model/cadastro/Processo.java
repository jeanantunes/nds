package br.com.abril.nds.model.cadastro;

public enum Processo {
	
	//TODO definir os nomes de processo
	PROCESSO("Processo a ser Definido")
	;
	
	private String processo;
	
	Processo(String processo) {
		this.processo = processo;
	}
	
	public String getDescricao(){
		return this.processo;
	}
	
}
