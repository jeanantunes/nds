package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class EspecialidadeDTO implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Long> especialidades;

	/**
	 * @return the especialidades
	 */
	public List<Long> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(List<Long> especialidades) {
		this.especialidades = especialidades;
	}
	
	
}
