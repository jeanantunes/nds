package br.com.abril.nds.model.cadastro;

public enum TipoDistribuicaoCota {
	
	CONVENCIONAL("Convencional"),
	ALTERNATIVO("Alternativo");
	
	
	private String descricao;
	
	private TipoDistribuicaoCota(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoDistribuicaoCota(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}