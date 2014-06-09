package br.com.abril.nds.model.estoque;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum OperacaoEstoque {
	ENTRADA("Entrada"),
	SAIDA("Saída");
	
	
	private String descricao;
	
	private OperacaoEstoque(String descricao) {

		this.descricao = descricao;
	}

	@Override
	public String toString() {

		return this.descricao;
	}
}