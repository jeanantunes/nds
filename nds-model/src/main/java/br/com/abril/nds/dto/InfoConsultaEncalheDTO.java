package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.vo.DebitoCreditoCotaVO;

public class InfoConsultaEncalheDTO {

	private List<ConsultaEncalheDTO> listaConsultaEncalhe;
	
	private List<DebitoCreditoCotaVO> listaDebitoCreditoCota;
	
	private Integer qtdeConsultaEncalhe;
	
	private BigDecimal valorReparte;
	
	private BigDecimal valorEncalhe;
	
	private BigDecimal valorVendaDia;
	
	private BigDecimal valorDebitoCredito;
	
	private BigDecimal valorPagar;
	
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
	 * @return the valorReparte
	 */
	public BigDecimal getValorReparte() {
		return valorReparte;
	}

	/**
	 * @param valorReparte the valorReparte to set
	 */
	public void setValorReparte(BigDecimal valorReparte) {
		this.valorReparte = valorReparte;
	}

	/**
	 * @return the valorEncalhe
	 */
	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	/**
	 * @param valorEncalhe the valorEncalhe to set
	 */
	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}

	/**
	 * @return the valorVendaDia
	 */
	public BigDecimal getValorVendaDia() {
		return valorVendaDia;
	}

	/**
	 * @param valorVendaDia the valorVendaDia to set
	 */
	public void setValorVendaDia(BigDecimal valorVendaDia) {
		this.valorVendaDia = valorVendaDia;
	}

	/**
	 * @return the valorDebitoCredito
	 */
	public BigDecimal getValorDebitoCredito() {
		return valorDebitoCredito;
	}

	/**
	 * @param valorDebitoCredito the valorDebitoCredito to set
	 */
	public void setValorDebitoCredito(BigDecimal valorDebitoCredito) {
		this.valorDebitoCredito = valorDebitoCredito;
	}

	/**
	 * @return the valorPagar
	 */
	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	/**
	 * @param valorPagar the valorPagar to set
	 */
	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	/**
	 * @return the listaDebitoCreditoCota
	 */
	public List<DebitoCreditoCotaVO> getListaDebitoCreditoCota() {
		return listaDebitoCreditoCota;
	}

	/**
	 * @param listaDebitoCreditoCota the listaDebitoCreditoCota to set
	 */
	public void setListaDebitoCreditoCota(
			List<DebitoCreditoCotaVO> listaDebitoCreditoCota) {
		this.listaDebitoCreditoCota = listaDebitoCreditoCota;
	}

}
