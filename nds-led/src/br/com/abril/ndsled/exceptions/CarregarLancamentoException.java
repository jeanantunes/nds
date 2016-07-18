package br.com.abril.ndsled.exceptions;

public class CarregarLancamentoException extends Exception {

	private static final long serialVersionUID = -5938101118771446724L;

	public CarregarLancamentoException() {
		super("N�o foi posssivel carregar o lan�amento!");
	}

	public CarregarLancamentoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CarregarLancamentoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CarregarLancamentoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
