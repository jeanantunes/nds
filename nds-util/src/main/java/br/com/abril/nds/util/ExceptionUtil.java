package br.com.abril.nds.util;

import java.io.PrintWriter;
import java.io.StringWriter;

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
	
	/**
	 * Obtém o stack trace de uma exceção.
	 * 
	 * @param e - exceção
	 * 
	 * @return Stack trace
	 */
	public static String getStackTrace(Throwable throwable) {
		
        StringWriter stringWriter = new StringWriter();
        
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        throwable.printStackTrace(printWriter);

        return stringWriter.toString();
    }
	
}
