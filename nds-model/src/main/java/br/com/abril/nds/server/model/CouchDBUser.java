package br.com.abril.nds.server.model;

import java.io.Serializable;

/**
 * Modelo que representa um usuário no CouchDB.
 * 
 * @author Discover Technology
 *
 */
public class CouchDBUser implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8855376576529603803L;

	private String idDistribuidor;

	/**
	 * @return the idDistribuidor
	 */
	public String getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(String idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

}
