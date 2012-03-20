package br.com.abril.nds.dto;

import java.math.BigDecimal;


public class ResumoBaixaBoletosDTO {

	private String nomeArquivo;
	
	private String dataCompetencia;
	
	private BigDecimal somaPagamentos;
	
	private Integer quantidadeLidos = 0;
	
	private Integer quantidadeBaixados = 0;
	
	private Integer quantidadeRejeitados = 0;

	private Integer quantidadeBaixadosComDivergencia = 0;

	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * @return the dataCompetencia
	 */
	public String getDataCompetencia() {
		return dataCompetencia;
	}

	/**
	 * @param dataCompetencia the dataCompetencia to set
	 */
	public void setDataCompetencia(String dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	/**
	 * @return the somaPagamentos
	 */
	public BigDecimal getSomaPagamentos() {
		return somaPagamentos;
	}

	/**
	 * @param somaPagamentos the somaPagamentos to set
	 */
	public void setSomaPagamentos(BigDecimal somaPagamentos) {
		this.somaPagamentos = somaPagamentos;
	}

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
