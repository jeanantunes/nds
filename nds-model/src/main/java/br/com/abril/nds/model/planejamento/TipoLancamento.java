package br.com.abril.nds.model.planejamento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoLancamento {
	
	LANCAMENTO("Lançamento"),
	SUPLEMENTAR("Suplementar"),
	RELANCAMENTO("Relançamento"),
	PARCIAL("Parcial"),
	REDISTRIBUICAO("Redistribuição");
	
	private String descricao;
	
	private TipoLancamento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}