package br.com.abril.nds.integracao.fileimporter;

import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

/**
 * Classe principal para executar as interfaces por linha de comando.<br>
 * Para executar uma interface, devem ser passados os seguintes parâmetros na linha de comando:<br>
 * <br>
 * 1) Nome do usuário     (obrigatório)<br>
 * 2) Código da interface (obrigatório)<br>
 * 3) Processa o retorno do ICD (-icdRetorno)
 * 4) Código do distribuidor (opcional) - se passado, carregará apenas os arquivos deste distribuidor<br>
 * <br>
 * Ex: java -jar ndsi-couchdbinterface.jar usuario 111 <br> 
 */
public class StartBatch {

	public static final String SPRING_FILE_LOCATION = "classpath:spring/applicationContext-ndsi-cli.xml"; 

	private static ApplicationContext applicationContext;

	static {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(SPRING_FILE_LOCATION);
		classPathXmlApplicationContext.registerShutdownHook();
		applicationContext = classPathXmlApplicationContext;
	}
	
	public static void main(String[] args) {
		
		Locale.setDefault(new Locale("en", "US")); 
		String usuario = null;
		Long codigoInterface = null;
		Long codigoDistribuidor = null;
		
		if (args == null || args.length < 2 || args.length > 4) {
			System.out.println("ERRO: numero de argumentos invalido");
			return;
		}
		
		usuario = args[0];
		
		try {
			codigoInterface = Long.valueOf(args[1]);
		} catch (NumberFormatException e)  {
			System.out.println("ERRO: codigo de interface invalido");
			return;
		}
		
		InterfaceEnum interfaceEnum = InterfaceEnum.getByCodigo(codigoInterface);
		if (interfaceEnum == null) {
			System.out.println("ERRO: interface invalida");
			return;
		}

		InterfaceExecutor executor = applicationContext.getBean(InterfaceExecutor.class);
		if (args.length == 4) {
			try {
				codigoDistribuidor = Long.valueOf(args[3]);
			} catch (NumberFormatException e)  {
				System.out.println("ERRO: codigo de distribuidor invalido");
				return;
			}
		}

		if (args[2].toString().equals("-icdRetorno")) {
			
			List<String> distribuidores = executor.recuperaDistribuidores(codigoDistribuidor);
			
			executor.executarRetornosIcd(distribuidores);
		} else {
			executor.executarInterface(usuario, interfaceEnum, codigoDistribuidor);
		}
		
	}
}
