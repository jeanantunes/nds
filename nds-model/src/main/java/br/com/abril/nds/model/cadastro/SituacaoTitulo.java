package br.com.abril.nds.model.cadastro;

/**
 * @author Discover Technology
 */
public enum SituacaoTitulo {
	
	EMITE("Emite"),
	CRIADO("Criado"),
	VINCULADO("Vinculado");

	private String situacao;
	
	private SituacaoTitulo(String situacao) {
		this.situacao = situacao;
	}
	
	public String getSituacao() {
		return this.situacao;
	}
}
