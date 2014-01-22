package br.com.abril.nds.model.planejamento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoLancamento {
	
	LANCAMENTO(1,"Lançamento"),
	REDISTRIBUICAO(2,"Redistribuição");
	
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