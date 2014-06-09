package br.com.abril.nds.service.exception;
/**
 * 
 * @author Diego Fernandes
 *
 */
public class UniqueConstraintViolationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7969593747114568535L;

	public UniqueConstraintViolationException() {
	}

	public UniqueConstraintViolationException(String message) {
		super(message);
	}

	public UniqueConstraintViolationException(Throwable cause) {
		super(cause);
	}

	public UniqueConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}
}
