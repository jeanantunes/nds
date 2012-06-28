package br.com.abril.nds.model.fiscal;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum StatusNotaFiscalEntrada {
	
	RECEBIDA("Notas recebidas"),
	
	NAO_RECEBIDA(""),
	
	PENDENTE(""),
	
	PENDENTE_RECEBIMENTO("Pendente Recebimento"),
	
	PENDENTE_EMISAO("Pendente Emissão");
	
	private StatusNotaFiscalEntrada(String descricao) {
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