package br.com.abril.nds.model.cadastro;

public enum TipoCobranca {
	
	BOLETO("Boleto"), 
	BOLETO_EM_BRANCO("Boleto em branco"), 
	CHEQUE("Cheque"), 
	DEPOSITO("Depósito"), 
	TRANSFERENCIA_BANCARIA("Transferência  Bancária "),
	DINHEIRO("Dinheiro"),
	OUTROS("Outros");
	
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