package br.com.abril.ndsled.exceptions;

public class CarregarCotaLedException extends Exception {

	private static final long serialVersionUID = -5251866320288283572L;

	public CarregarCotaLedException() {
		super("N�o foi poss�vel carregar a Lista de Cota Led.");
	}

	public CarregarCotaLedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarregarCotaLedException(String message) {
		super(message);
	}

	public CarregarCotaLedException(Throwable cause) {
		super(cause);
	}

}
