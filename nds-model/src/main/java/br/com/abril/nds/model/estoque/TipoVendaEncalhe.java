package br.com.abril.nds.model.estoque;

public enum TipoVendaEncalhe {
	
	ENCALHE("Encalhe"),
	SUPLEMENTAR("Suplementar");
	
	private String venda;
	
	private TipoVendaEncalhe(String venda) {
		this.venda = venda;
	}
	
	public String getVenda(){
		return this.venda;
	}
	
	@Override
	public String toString() {
		return this.venda;
	}
}
