package br.com.abril.nds.integracao.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Factory para Loggers dos batchs.
 * Mantém uma instância de ndsiLoggerFactory para batch em execução.
 * Utilizar para acessar gravação de log em tabela.
 * 
 * @author jonatas.junior
 */
@Component
public class NdsServerLoggerFactory {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private NdsServerLoggerFactory() {}

	private class ThreadLocalNdsLogger extends ThreadLocal<NdsServerLogger> {
		public NdsServerLogger initialValue() {
			return applicationContext.getBean(NdsServerLogger.class);
		}
	}
	
	private ThreadLocalNdsLogger threadLocalNdsLogger = new ThreadLocalNdsLogger();
	
	/**
	 * Retorna uma instância do ndsiLoggerFactory referente ao batch em execução.
	 * 
	 * @return logger
	 */
	public NdsServerLogger getLogger() {
		return threadLocalNdsLogger.get();
	}
	
	public void resetLogger() {
		threadLocalNdsLogger = new ThreadLocalNdsLogger();
	}
	
}
