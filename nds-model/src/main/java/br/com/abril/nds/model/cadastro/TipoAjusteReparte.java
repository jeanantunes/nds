package br.com.abril.nds.model.cadastro;

public enum TipoAjusteReparte {
	
	AJUSTE_HISTORICO("historico"),
	AJUSTE_SEGMENTO("segmento"),
	AJUSTE_VENDA_MEDIA("vendaMedia"),
	AJUSTE_ENCALHE_MAX("encalheMaximo");

	private String descricao;
	
	private TipoAjusteReparte(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
}
