package br.com.abril.nds.integracao.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * Classe principal para executar as interfaces por linha de comando.<br>
 * Para executar uma interface, devem ser passados os seguintes parâmetros na linha de comando:<br>
 * <br>
 * 1) Código da interface (obrigatório)<br>
 * 2) Nome do usuário     (Opcional)<br>
 * <br>
 * Ex: java -jar nds-integration-client.jar 111 usuario <br>
 */
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
		
		if (args.length > 1) {
			username = args[1];
		}

		DistribuidorService distribuidorService = getDistribuidorService();
		
		String codigoDistribuidorDinap = distribuidorService.codigoDistribuidorDinap();
		
		if (codigoDistribuidorDinap != null) {

			getRouteTemplate(args[0]).execute(username, codigoDistribuidorDinap);
		}
		
		String codigoDistribuidorFC = distribuidorService.codigoDistribuidorFC();
		
		if (codigoDistribuidorFC != null) {
		
			getRouteTemplate(args[0]).execute(username, codigoDistribuidorFC);
		}
		
		System.exit(0);
	}
	
	private static RouteTemplate getRouteTemplate(String className) {
		try {
			return (RouteTemplate) applicationContext.getBean(Class.forName(className));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static DistribuidorService getDistribuidorService() {
		
		try {
			return (DistribuidorService) applicationContext.getBean(DistribuidorService.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}