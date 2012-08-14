package br.com.abril.nds.model.cadastro;

public enum TipoAtividade {

	PRESTADOR_SERVICO("Prestador Serviço"),
	MERCANTIL("Mercantil");
	
	private String descricao;
	
	private TipoAtividade(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoDistribuidor() {
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
