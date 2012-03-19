package br.com.abril.nds.dto;


public class ResumoBaixaBoletosDTO {

	private Integer quantidadeLidos = 0;
	
	private Integer quantidadeBaixados = 0;
	
	private Integer quantidadeRejeitados = 0;

	private Integer quantidadeBaixadosComDivergencia = 0;

	/**
	 * @return the quantidadeLidos
	 */
	public Integer getQuantidadeLidos() {
		return quantidadeLidos;
	}

	/**
	 * @param quantidadeLidos the quantidadeLidos to set
	 */
	public void setQuantidadeLidos(Integer quantidadeLidos) {
		this.quantidadeLidos = quantidadeLidos;
	}

	/**
	 * @return the quantidadeBaixados
	 */
	public Integer getQuantidadeBaixados() {
		return quantidadeBaixados;
	}

	/**
	 * @param quantidadeBaixados the quantidadeBaixados to set
	 */
	public void setQuantidadeBaixados(Integer quantidadeBaixados) {
		this.quantidadeBaixados = quantidadeBaixados;
	}

	/**
	 * @return the quantidadeRejeitados
	 */
	public Integer getQuantidadeRejeitados() {
		return quantidadeRejeitados;
	}

	/**
	 * @param quantidadeRejeitados the quantidadeRejeitados to set
	 */
	public void setQuantidadeRejeitados(Integer quantidadeRejeitados) {
		this.quantidadeRejeitados = quantidadeRejeitados;
	}

	/**
	 * @return the quantidadeBaixadosComDivergencia
	 */
	public Integer getQuantidadeBaixadosComDivergencia() {
		return quantidadeBaixadosComDivergencia;
	}

	/**
	 * @param quantidadeBaixadosComDivergencia the quantidadeBaixadosComDivergencia to set
	 */
	public void setQuantidadeBaixadosComDivergencia(
			Integer quantidadeBaixadosComDivergencia) {
		this.quantidadeBaixadosComDivergencia = quantidadeBaixadosComDivergencia;
	}
	
}
