package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class CalendarioFeriadoWrapper implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String descricaoMes;
	
	private List<CalendarioFeriadoDTO> listaCalendarioFeriado;

	/**
	 * Obtém descricaoMes
	 *
	 * @return String
	 */
	public String getDescricaoMes() {
		return descricaoMes;
	}

	/**
	 * Atribuí descricaoMes
	 * @param descricaoMes 
	 */
	public void setDescricaoMes(String descricaoMes) {
		this.descricaoMes = descricaoMes;
	}

	/**
	 * Obtém listaCalendarioFeriado
	 *
	 * @return List<CalendarioFeriadoDTO>
	 */
	public List<CalendarioFeriadoDTO> getListaCalendarioFeriado() {
		return listaCalendarioFeriado;
	}

	/**
	 * Atribuí listaCalendarioFeriado
	 * @param listaCalendarioFeriado 
	 */
	public void setListaCalendarioFeriado(
			List<CalendarioFeriadoDTO> listaCalendarioFeriado) {
		this.listaCalendarioFeriado = listaCalendarioFeriado;
	}
	
	
	
}
