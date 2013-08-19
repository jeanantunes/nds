package br.com.abril.nds.service.exception;

public class AutenticacaoEmailException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	public AutenticacaoEmailException(String msg) {
		super(msg);
	}
	
	public AutenticacaoEmailException(Throwable throwable) {
		super(throwable);
	}
	
	public AutenticacaoEmailException(String msg,Throwable throwable) {
		super(msg,throwable);
	}
}
