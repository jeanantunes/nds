package br.com.abril.nds.service.exception;

public class FechamentoDiarioException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FechamentoDiarioException() {}
	
	public FechamentoDiarioException(String message) {
		super(message);
	}
	
	public FechamentoDiarioException(String message, Throwable cause) {
		super(message,cause);
	}
}
