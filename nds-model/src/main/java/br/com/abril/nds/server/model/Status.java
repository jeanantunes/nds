package br.com.abril.nds.server.model;

public enum Status {

	ENCERRADO("Encerrado"),
	FECHAMENTO("Fechamento"),
	//OFFLINE("Off-line"),
	OPERANDO("Operando");
	
	private String descricao;
	
	private Status(String descricao){
		
		this.descricao = descricao;
	}
	
	public String toString(){
		
		return this.descricao;
	}
}