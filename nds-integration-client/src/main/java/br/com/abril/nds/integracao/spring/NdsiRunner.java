package br.com.abril.nds.integracao.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.repository.DistribuidorRepository;


public abstract class NdsiRunner {
	public static final String SPRING_FILE_LOCATION = "classpath:spring/applicationContext-ndsi-cli.xml"; 
	
	private static ApplicationContext applicationContext;
	
	public static String USER_NAME = "ndsiMainUser";
	
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
		
		String username = USER_NAME;
		
		if (args.length > 0) {
			username = args[1];
		}

		getRouteTemplate(args[0]).execute(
			username, getDistribuidorRepository().codigoDistribuidorDinap());
		
		getRouteTemplate(args[0]).execute(
			username, getDistribuidorRepository().codigoDistribuidorFC());
	}
	
	private static RouteTemplate getRouteTemplate(String className) {
		try {
			return (RouteTemplate) applicationContext.getBean(Class.forName(className));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static DistribuidorRepository getDistribuidorRepository() {
		
		try {
			return (DistribuidorRepository) applicationContext.getBean(DistribuidorRepository.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}