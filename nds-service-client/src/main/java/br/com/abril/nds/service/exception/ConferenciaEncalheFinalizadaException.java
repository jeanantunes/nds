package br.com.abril.nds.service.exception;

public class ConferenciaEncalheFinalizadaException extends Exception  {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ConferenciaEncalheFinalizadaException() {
		super();
	}

	public ConferenciaEncalheFinalizadaException(String message, Throwable e) {
		super(message, e);
	}

	public ConferenciaEncalheFinalizadaException(String message) {
		super(message);
	}

	public ConferenciaEncalheFinalizadaException(Throwable e) {
		super(e);
	}
	
	
}
