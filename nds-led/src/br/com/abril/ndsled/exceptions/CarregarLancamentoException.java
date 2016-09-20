package br.com.abril.ndsled.exceptions;

public class CarregarLancamentoException extends Exception {

	private static final long serialVersionUID = -5938101118771446724L;

	public CarregarLancamentoException() {
		super("Não foi posssivel carregar o lançamento!");
	}

	public CarregarLancamentoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarregarLancamentoException(String message) {
		super(message);
	}

	public CarregarLancamentoException(Throwable cause) {
		super(cause);
	}

}
