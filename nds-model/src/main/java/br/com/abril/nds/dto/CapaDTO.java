package br.com.abril.nds.dto;

import java.io.Serializable;

public class CapaDTO implements Serializable {

	private static final long serialVersionUID = 3340074459202508991L;

	private Long id;
	private Integer sequenciaMatriz;

	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}
	
}
