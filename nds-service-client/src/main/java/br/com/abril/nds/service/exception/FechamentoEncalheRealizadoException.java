package br.com.abril.nds.service.exception;

/**
 * Exception lançada devido a tentativa de realizar uma reabertura ou nova conferencia
 * de encalhe em uma data cujo fechamento de encalhe ja tenha sido criado.
 *  
 * @author Discover Technology
 *
 */
public class FechamentoEncalheRealizadoException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public FechamentoEncalheRealizadoException() {
		super();
	}

	public FechamentoEncalheRealizadoException(String message, Throwable e) {
		super(message, e);
	}

	public FechamentoEncalheRealizadoException(String message) {
		super(message);
	}

	public FechamentoEncalheRealizadoException(Throwable e) {
		super(e);
	}
	
}
