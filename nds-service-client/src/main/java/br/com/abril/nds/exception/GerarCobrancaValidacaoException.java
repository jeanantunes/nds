package br.com.abril.nds.exception;

public class GerarCobrancaValidacaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039379246787865347L;
	
	private ValidacaoException validacaoException;
	
	public GerarCobrancaValidacaoException(ValidacaoException validacaoException){
		
		this.validacaoException = validacaoException;
	}

	public ValidacaoException getValidacaoException() {
		return validacaoException;
	}
}