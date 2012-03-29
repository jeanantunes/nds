package br.com.abril.nds.model.cadastro;

public enum TipoCobranca {
	
	BOLETO("Boleto"), 
	CHEQUE("Cheque"),
	DINHEIRO("Dinheiro"), 
	DEPOSITO("Depósito"), 
	TRANSFERENCIA_BANCARIA("Transferência  Bancaria ");
	
	private String descricao;
	
	private TipoCobranca(String descricao) {
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