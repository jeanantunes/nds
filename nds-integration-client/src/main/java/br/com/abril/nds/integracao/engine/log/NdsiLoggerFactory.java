package br.com.abril.nds.integracao.engine.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Factory para Loggers dos batchs.
 * Mantém uma instância de NdsiLogger para batch em execução.
 * Utilizar para acessar gravação de log em tabela.
 * 
 * @author jonatas.junior
 */
@Component
public class NdsiLoggerFactory {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private NdsiLoggerFactory() {}

	private class ThreadLocalNdsLogger extends ThreadLocal<NdsiLogger> {
		public NdsiLogger initialValue() {
			return applicationContext.getBean(NdsiLogger.class);
		}
	}
	
	private ThreadLocalNdsLogger threadLocalNdsLogger = new ThreadLocalNdsLogger();
	
	/**
	 * Retorna uma instância do NdsiLogger referente ao batch em execução.
	 * 
	 * @return logger
	 */
	public NdsiLogger getLogger() {
		return threadLocalNdsLogger.get();
	}
	
	public void resetLogger() {
		threadLocalNdsLogger = new ThreadLocalNdsLogger();
	}
	
}
