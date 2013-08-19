package br.com.abril.nds.model.planejamento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoLancamento {
	
	NORMAL(1,"Redistribuição"),
	PAC_KIT_PROM_OF(2,"Redistribuição"),
	RELANCAMENTO(3,"Relançamento"),
	FASE(4,"Redistribuição"),
	ESPECIAL(5,"Redistribuição"),
	REEDICAO(6,"Redistribuição"),
	ENCADERNACÃO(7,"Redistribuição"),
	TESTES(8,"Redistribuição"),
	LANCAMENTO(9,"Lançamento"),
	SUPLEMENTAR(10,"Suplementar"),
	PARCIAL(11,"Parcial"),
	REDISTRIBUICAO(12,"Redistribuição");

	private Integer codigo;
	
	private String descricao;
	
	private TipoLancamento(Integer codigo, String descricao) {
		
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}	
	
}