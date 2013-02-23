package br.com.abril.nds.model.integracao;

public enum StatusExecucaoEnum {

	SUCESSO("S"),
	AVISO("A"),
	FALHA("F");
	
	private String descricao;
	
	private StatusExecucaoEnum(String desc) {
		this.descricao = desc;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
	public static StatusExecucaoEnum fromString(String status) {
		if ("S".equals(status)) {
			return SUCESSO;
		}
		else if ("A".equals(status)) {
			return AVISO;
		}
		else if ("F".equals(status)) {
			return FALHA;
		}
		
		return null;
	}
}
