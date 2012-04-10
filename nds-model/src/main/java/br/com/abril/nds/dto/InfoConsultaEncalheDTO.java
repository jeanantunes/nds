package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

public class InfoConsultaEncalheDTO {

	private List<ConsultaEncalheDTO> listaConsultaEncalhe;
	
	private Integer qtdeConsultaEncalhe;
	
	private BigDecimal qtdProdutoPrimeiroRecolhimento;
	
	private BigDecimal qtdExemplarPrimeiroRecolhimento;

	private BigDecimal qtdProdutoDemaisRecolhimentos;
	
	private BigDecimal qtdExemplarDemaisRecolhimentos;

	/**
	 * Obtém listaConsultaEncalhe
	 *
	 * @return List<ConsultaEncalheDTO>
	 */
	public List<ConsultaEncalheDTO> getListaConsultaEncalhe() {
		return listaConsultaEncalhe;
	}

	/**
	 * Atribuí listaConsultaEncalhe
	 * @param listaConsultaEncalhe 
	 */
	public void setListaConsultaEncalhe(
			List<ConsultaEncalheDTO> listaConsultaEncalhe) {
		this.listaConsultaEncalhe = listaConsultaEncalhe;
	}

	/**
	 * Obtém qtdeConsultaEncalhe
	 *
	 * @return Integer
	 */
	public Integer getQtdeConsultaEncalhe() {
		return qtdeConsultaEncalhe;
	}

	/**
	 * Atribuí qtdeConsultaEncalhe
	 * @param qtdeConsultaEncalhe 
	 */
	public void setQtdeConsultaEncalhe(Integer qtdeConsultaEncalhe) {
		this.qtdeConsultaEncalhe = qtdeConsultaEncalhe;
	}

	/**
	 * Obtém qtdProdutoPrimeiroRecolhimento
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdProdutoPrimeiroRecolhimento() {
		return qtdProdutoPrimeiroRecolhimento;
	}

	/**
	 * Atribuí qtdProdutoPrimeiroRecolhimento
	 * @param qtdProdutoPrimeiroRecolhimento 
	 */
	public void setQtdProdutoPrimeiroRecolhimento(
			BigDecimal qtdProdutoPrimeiroRecolhimento) {
		this.qtdProdutoPrimeiroRecolhimento = qtdProdutoPrimeiroRecolhimento;
	}

	/**
	 * Obtém qtdExemplarPrimeiroRecolhimento
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdExemplarPrimeiroRecolhimento() {
		return qtdExemplarPrimeiroRecolhimento;
	}

	/**
	 * Atribuí qtdExemplarPrimeiroRecolhimento
	 * @param qtdExemplarPrimeiroRecolhimento 
	 */
	public void setQtdExemplarPrimeiroRecolhimento(
			BigDecimal qtdExemplarPrimeiroRecolhimento) {
		this.qtdExemplarPrimeiroRecolhimento = qtdExemplarPrimeiroRecolhimento;
	}

	/**
	 * Obtém qtdProdutoDemaisRecolhimentos
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdProdutoDemaisRecolhimentos() {
		return qtdProdutoDemaisRecolhimentos;
	}

	/**
	 * Atribuí qtdProdutoDemaisRecolhimentos
	 * @param qtdProdutoDemaisRecolhimentos 
	 */
	public void setQtdProdutoDemaisRecolhimentos(
			BigDecimal qtdProdutoDemaisRecolhimentos) {
		this.qtdProdutoDemaisRecolhimentos = qtdProdutoDemaisRecolhimentos;
	}

	/**
	 * Obtém qtdExemplarDemaisRecolhimentos
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdExemplarDemaisRecolhimentos() {
		return qtdExemplarDemaisRecolhimentos;
	}

	/**
	 * Atribuí qtdExemplarDemaisRecolhimentos
	 * @param qtdExemplarDemaisRecolhimentos 
	 */
	public void setQtdExemplarDemaisRecolhimentos(
			BigDecimal qtdExemplarDemaisRecolhimentos) {
		this.qtdExemplarDemaisRecolhimentos = qtdExemplarDemaisRecolhimentos;
	}
	
	
	
	

	
}
