package br.com.abril.nds.model.cadastro;

public enum TipoParametrosDistribuidorEmissaoDocumento {

	SLIP("Slip"),
	BOLETO("Boleto"),
	BOLETO_SLIP("Boleto + Slip"),
	RECIBO("Recibo"),
	NOTA_ENVIO("Nota de Envio"),
	CHAMADA_ENCALHE("Chamada de Encalhe");
	
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
