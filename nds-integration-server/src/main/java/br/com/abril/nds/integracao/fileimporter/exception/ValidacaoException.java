package br.com.abril.nds.integracao.fileimporter.exception;

/**
 * Exceção gerada na validação das linhas dos arquivos durante a leitura.
 */
public class ValidacaoException extends Exception {

	private static final long serialVersionUID = 2479902031886743859L;

	public ValidacaoException() {
		super();
	}

	public ValidacaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidacaoException(String message) {
		super(message);
	}

	public ValidacaoException(Throwable cause) {
		super(cause);
	}
}
