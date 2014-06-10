package br.com.abril.nds.model.fiscal;

/**
 * @author Robson Martins
 * @version 1.0
 */
public enum StatusRecebimento {
	
	SALVO("Salvo"),
	
	CONFIRMADO("Confirmado");
	
	private StatusRecebimento(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}
}