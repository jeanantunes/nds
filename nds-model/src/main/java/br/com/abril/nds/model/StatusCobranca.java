package br.com.abril.nds.model;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 08-mar-2012 14:45:33
 */
public enum StatusCobranca {
	
	PAGO("Pago"),
	NAO_PAGO("Não pago"),
	EMITIDO("Emitido");
	
	private String descricao;
	
	private StatusCobranca(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
	
		return this.descricao;
	}
	
}