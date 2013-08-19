package br.com.abril.nds.model.cadastro;

public enum TipoCobrancaCotaGarantia {
	
	DINHEIRO("Dinheiro"),
	DEPOSITO_TRANSFERENCIA("Depósito / Transferência"),
	BOLETO("Boleto"), 
	DESCONTO_COTA("% Desconto da Cota");
	
	private String descricao;
	
	private TipoCobrancaCotaGarantia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoCobranca(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}