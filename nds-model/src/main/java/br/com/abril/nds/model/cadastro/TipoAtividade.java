package br.com.abril.nds.model.cadastro;

public enum TipoAtividade {

	PRESTADOR_SERVICO("Prestador Serviço"),
	MERCANTIL("Mercantil");
	
	private String descricao;
	
	private TipoAtividade(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
