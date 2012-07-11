package br.com.abril.nds.exception;

import java.io.Serializable;

public class ImportacaoException extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ImportacaoException(String message) {
		super(message);
	}
}
