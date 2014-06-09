package br.com.abril.nds.model.cadastro;

public enum TipoCota {
	
	CONSIGNADO("Consignado"),
	A_VISTA("À vista");
	
	private String descricao;
	
	private TipoCota(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoCota(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}