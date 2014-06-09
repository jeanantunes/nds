package br.com.abril.nds.model.fiscal.nota;

public enum TipoEncargoFinanceiro {

	SOBRE_PRODUTO("P"),
	SOBRE_SERVICO("S");
	
	private String sigla;
	
	private TipoEncargoFinanceiro(String sigla) {
		
		this.sigla = sigla;
	}
	
	@Override
	public String toString() {
	
		return this.sigla;
	}
	
}
