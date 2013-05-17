package br.com.abril.nds.exception;

public class EnvioEmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnvioEmailException() {
	}

	public EnvioEmailException(String msg) {
		super(msg);
	}
	
	public EnvioEmailException(Throwable throwable) {
		super(throwable);
	}
	
	public EnvioEmailException(String msg,Throwable throwable) {
		super(msg,throwable);
	}

}
