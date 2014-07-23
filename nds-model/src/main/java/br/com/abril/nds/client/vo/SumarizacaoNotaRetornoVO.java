package br.com.abril.nds.client.vo;

/**
 * Value Object para a sumarização das NFe de retorno, formatados para exibir na tela.
 * 
 * @author Discover Technology
 */
public class SumarizacaoNotaRetornoVO {

	private Long numeroTotalArquivos;
	
	private Long numeroNotasAprovadas;
	
	private Long numeroNotasRejeitadas;

	/**
	 * @return the numeroTotalArquivos
	 */
	public Long getNumeroTotalArquivos() {
		return numeroTotalArquivos;
	}

	/**
	 * @param numeroTotalArquivos the numeroTotalArquivos to set
	 */
	public void setNumeroTotalArquivos(Long numeroTotalArquivos) {
		this.numeroTotalArquivos = numeroTotalArquivos;
	}

	/**
	 * @return the numeroNotasAprovadas
	 */
	public Long getNumeroNotasAprovadas() {
		return numeroNotasAprovadas;
	}

	/**
	 * @param numeroNotasAprovadas the numeroNotasAprovadas to set
	 */
	public void setNumeroNotasAprovadas(Long numeroNotasAprovadas) {
		this.numeroNotasAprovadas = numeroNotasAprovadas;
	}

	/**
	 * @return the numeroNotasRejeitadas
	 */
	public Long getNumeroNotasRejeitadas() {
		return numeroNotasRejeitadas;
	}

	/**
	 * @param numeroNotasRejeitadas the numeroNotasRejeitadas to set
	 */
	public void setNumeroNotasRejeitadas(Long numeroNotasRejeitadas) {
		this.numeroNotasRejeitadas = numeroNotasRejeitadas;
	}
	
}
