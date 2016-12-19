package br.com.abril.nds.model.cadastro;

public enum TipoCobranca {
	
	BOLETO("Boleto"), 
	BOLETO_EM_BRANCO("Boleto em branco"),
	CHEQUE("Cheque"), 
	DEPOSITO("Deposito"), 
	TRANSFERENCIA_BANCARIA("Transferencia Bancaria"),
	DINHEIRO("Dinheiro"),
	OUTROS("Outros"),
	BOLETO_AVULSO("Boleto Avulso"),
	BOLETO_NFE("Boleto NFe");
	
	private String descricao;
	
	private TipoCobranca(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoCobranca(){
		return this.descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}