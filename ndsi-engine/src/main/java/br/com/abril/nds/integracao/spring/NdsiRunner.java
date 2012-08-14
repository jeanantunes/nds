package br.com.abril.nds.integracao.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;


public abstract class NdsiRunner {
	public static final String SPRING_FILE_LOCATION = "classpath:spring/applicationContext-ndsi-cli.xml"; 
	
	private static ApplicationContext applicationContext;
	
	static {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = 
				new ClassPathXmlApplicationContext(SPRING_FILE_LOCATION);
		
		classPathXmlApplicationContext.registerShutdownHook();
		
		applicationContext = classPathXmlApplicationContext;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new RuntimeException("Informe o nome da rota a ser executada.");
		}
		
		String username = "ndsiMainUser";
		
		if (args.length > 1) {
			username = args[1];
		}
		
		getRouteTemplate(args[0]).execute(username);
	}
	
	private static RouteTemplate getRouteTemplate(String className) {
		try {
			return (RouteTemplate) applicationContext.getBean(Class.forName(className));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}