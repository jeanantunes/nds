package br.com.abril.nds.dto;

import java.math.BigDecimal;


public class ResumoBaixaBoletosDTO {

	private String nomeArquivo;
	
	private String dataCompetencia;
	
	private BigDecimal somaPagamentos;
	
	private Integer quantidadePrevisao;
	
	private Integer quantidadeLidos;
	
	private Integer quantidadeBaixados;
	
	private Integer quantidadeRejeitados;

	private Integer quantidadeBaixadosComDivergencia;
	
	private Integer quantidadeInadimplentes;
	
	private BigDecimal valorTotalBancario;

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
	 * @return the quantidadePrevisao
	 */
	public Integer getQuantidadePrevisao() {
		return quantidadePrevisao;
	}

	/**
	 * @param quantidadePrevisao the quantidadePrevisao to set
	 */
	public void setQuantidadePrevisao(Integer quantidadePrevisao) {
		this.quantidadePrevisao = quantidadePrevisao;
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

	/**
	 * @return the quantidadeInadimplentes
	 */
	public Integer getQuantidadeInadimplentes() {
		return quantidadeInadimplentes;
	}

	/**
	 * @param quantidadeInadimplentes the quantidadeInadimplentes to set
	 */
	public void setQuantidadeInadimplentes(Integer quantidadeInadimplentes) {
		this.quantidadeInadimplentes = quantidadeInadimplentes;
	}

	/**
	 * @return the valorTotalBancario
	 */
	public BigDecimal getValorTotalBancario() {
		return valorTotalBancario;
	}

	/**
	 * @param valorTotalBancario the valorTotalBancario to set
	 */
	public void setValorTotalBancario(BigDecimal valorTotalBancario) {
		this.valorTotalBancario = valorTotalBancario;
	}
	
}
