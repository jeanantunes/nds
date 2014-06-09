package br.com.abril.nds.model.planejamento;

/**
 * Tipos de lançamentos parciais disponíveis
 * 
 * @author francisco.garcia
 *
 */
public enum TipoLancamentoParcial {
	
	PARCIAL("Parcial"),
	FINAL("Final");

	private String descricao;
	
	private TipoLancamentoParcial(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
}
