package br.com.abril.nds.service.exception;

public class EnderecoUniqueConstraintViolationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7969593747114568535L;

	public EnderecoUniqueConstraintViolationException() {
	}

	public EnderecoUniqueConstraintViolationException(String message) {
		super(message);
	}

	public EnderecoUniqueConstraintViolationException(Throwable cause) {
		super(cause);
	}

	public EnderecoUniqueConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}
}
