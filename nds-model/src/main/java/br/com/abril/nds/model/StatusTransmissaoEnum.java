package br.com.abril.nds.model;

public enum StatusTransmissaoEnum {


	PENDENTE("PENDENTE"),
	TRANSMITIDO("TRANSMITIDO"),
	//OFFLINE("Off-line"),
	NAO_TRANSMITIDO("NAO_TRANSMITIDO");
	
	private String descricao;
	
	private StatusTransmissaoEnum(String descricao){
		
		this.descricao = descricao;
	}
	
	public String toString(){
		
		return this.descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
