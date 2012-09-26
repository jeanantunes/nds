package br.com.abril.nds.dto;

import java.util.List;

public class InfoConsultaEncalheDTO {

	private List<ConsultaEncalheDTO> listaConsultaEncalhe;
	
	private Integer qtdeConsultaEncalhe;
	
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

	
}
