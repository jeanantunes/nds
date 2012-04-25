package br.com.abril.nds.dto;

import java.io.Serializable;

public class EspecialidadeDTO implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long especialidade;

	/**
	 * @return the especialidade
	 */
	public Long getEspecialidade() {
		return especialidade;
	}

	/**
	 * @param especialidade the especialidade to set
	 */
	public void setEspecialidade(Long especialidade) {
		this.especialidade = especialidade;
	}

	

		
	
}
