package br.com.abril.nds.dto;

import java.io.Serializable;

public class EntregadorDTO implements Serializable {

	private static final long serialVersionUID = 4358177424603794804L;

	private Long idEntregador;
	
	private String nomeEntregador;

	public EntregadorDTO() {
		
	}
			
	public EntregadorDTO(Long idEntregador, String nomeEntregador) {
		super();
		this.idEntregador = idEntregador;
		this.nomeEntregador = nomeEntregador;
	}

	/**
	 * @return the idEntregador
	 */
	public Long getIdEntregador() {
		return idEntregador;
	}

	/**
	 * @param idEntregador the idEntregador to set
	 */
	public void setIdEntregador(Long idEntregador) {
		this.idEntregador = idEntregador;
	}

	/**
	 * @return the nomeEntregador
	 */
	public String getNomeEntregador() {
		return nomeEntregador;
	}

	/**
	 * @param nomeEntregador the nomeEntregador to set
	 */
	public void setNomeEntregador(String nomeEntregador) {
		this.nomeEntregador = nomeEntregador;
	}
}
