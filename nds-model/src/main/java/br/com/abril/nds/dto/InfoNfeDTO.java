package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class InfoNfeDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Integer qtdeRegistros;
	
	private List<NfeDTO> listaNfeDTO;

	/**
	 * Obtém qtdeRegistros
	 *
	 * @return Integer
	 */
	public Integer getQtdeRegistros() {
		return qtdeRegistros;
	}

	/**
	 * Atribuí qtdeRegistros
	 * @param qtdeRegistros 
	 */
	public void setQtdeRegistros(Integer qtdeRegistros) {
		this.qtdeRegistros = qtdeRegistros;
	}

	/**
	 * Obtém listaNfeDTO
	 *
	 * @return List<NfeDTO>
	 */
	public List<NfeDTO> getListaNfeDTO() {
		return listaNfeDTO;
	}

	/**
	 * Atribuí listaNfeDTO
	 * @param listaNfeDTO 
	 */
	public void setListaNfeDTO(List<NfeDTO> listaNfeDTO) {
		this.listaNfeDTO = listaNfeDTO;
	}
	
	
	
}
