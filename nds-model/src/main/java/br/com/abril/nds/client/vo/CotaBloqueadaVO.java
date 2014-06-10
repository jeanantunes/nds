package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class CotaBloqueadaVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nomeUsuario;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	/**
	 * @param nomeUsuario
	 * @param numeroCota
	 * @param nomeCota
	 */
	public CotaBloqueadaVO(String nomeUsuario, Integer numeroCota,
			String nomeCota) {
		super();
		this.nomeUsuario = nomeUsuario;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the nomeUsuario
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @param nomeUsuario the nomeUsuario to set
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomecota() {
		return nomeCota;
	}

	/**
	 * @param nomecota the nomeCota to set
	 */
	public void setNomecota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
}
