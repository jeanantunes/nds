package br.com.abril.nds.model.estoque;

public enum TipoVendaEncalhe {
	
	ENCALHE("Venda de Encalhe"),
	SUPLEMENTAR("Venda de Suplementar");
	
	private String venda;
	
	private TipoVendaEncalhe(String venda) {
		this.venda = venda;
	}
	
	public String getVenda(){
		return this.venda;
	}
}
