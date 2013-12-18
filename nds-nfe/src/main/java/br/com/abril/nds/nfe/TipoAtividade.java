package br.com.abril.nds.nfe;

public enum TipoAtividade {
	
	MERCANTIL("Mercantil"),
	PRESTADOR_SERVICO("Prestador Serviço"),
	PRESTADOR_FILIAL("Prestador de Serviços - Filial");
	
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

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
