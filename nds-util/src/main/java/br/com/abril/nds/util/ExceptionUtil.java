package br.com.abril.nds.util;


/**
 * Classe utilitária que lida com as complexidades de exceções.
 * 
 * @author Discover Technology
 * 
 */
public abstract class ExceptionUtil {

	    /**
     * Obtém a exceção raiz.
     * 
     * @param throwable - throwable
     * 
     * @return Throwable
     */
	public static Throwable getRootCause(Throwable throwable) {
	    
		while (throwable.getCause() != null) {
			
			throwable = throwable.getCause();
	    }
		
	    return throwable;
	}
	
}
