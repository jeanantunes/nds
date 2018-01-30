package br.com.abril.nds.model.cadastro;

public enum TipoParametrosDistribuidorEmissaoDocumento {

	BOLETO("Boleto"),
	BOLETO_SLIP("Boleto + Slip"),
	CHAMADA_ENCALHE("Chamada de Encalhe"),
	NOTA_ENVIO("Nota de Envio"),
	RECIBO("Recibo"),
	SLIP("Slip");

	private String descricao;
	
	private TipoParametrosDistribuidorEmissaoDocumento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoParametrosDistribuidorEmissaoDocumento(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}	
	
}
