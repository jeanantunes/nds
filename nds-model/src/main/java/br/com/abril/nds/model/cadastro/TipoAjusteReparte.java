package br.com.abril.nds.model.cadastro;

public enum TipoAjusteReparte {
	
	AJUSTE_HISTORICO("Histórico"),
	AJUSTE_SEGMENTO("Segmento"),
	AJUSTE_VENDA_MEDIA("Venda Média"),
	AJUSTE_ENCALHE_MAX("Encalhe Máximo");

	private String descricao;
	
	private TipoAjusteReparte(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
}
