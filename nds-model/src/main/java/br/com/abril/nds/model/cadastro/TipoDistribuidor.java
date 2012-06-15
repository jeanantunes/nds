package br.com.abril.nds.model.cadastro;

public enum TipoDistribuidor {

	PRESTADOR_SERVICO("Prestador Serviço"),
	MERCANTIL("Mercantil");
	
	private String descricao;
	
	private TipoDistribuidor(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoDistribuidor() {
		return this.descricao;
	}
	
}
