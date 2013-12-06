package br.com.abril.nds.integracao.engine.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.DynamicRouter;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.route.RouteParameterProvider;
import br.com.abril.nds.repository.AbstractRepository;

@Component
@Scope("prototype")
public abstract class RouteTemplate extends AbstractRepository {
	
	@Autowired
	private RouteParameterProvider parameterProvider;

	@Autowired
	private DynamicRouter dynamicRouter;
	
	private String userName;
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	private String codigoDistribuidor;
	
	/**
	 * Metodo para configurar a rota da mensagem
	 * 
	 * 		public void setupRoute() {
	 *			setMessageProcessor(new TestProcessor());
	 *			setUri("c:/test/test.txt");
	 *			addTypeMapping(new FixedLengthField(1, 1, "A"), TestFixedModelHeader.class);
	 *			addTypeMapping(new FixedLengthField(1, 1, "B"), TestFixedModelDetail.class);
	 *			addTypeMapping(new FixedLengthField(1, 1, "C"), TestFixedModelFooter.class);				
	 *		}
	 *
	 */
	public void setupRoute() {
		// OBTEM OS PARAMETROS DO BANCO DE DADOS
		parameters.putAll(parameterProvider.getParameters());
		
		// CONFIGURA OS MAPEAMENTOS DE TIPO DE REGISTRO
		setupTypeMapping();
	}
	
	public boolean isCommitAtEnd() {
		return false;
	}

	public boolean isBulkLoad() {
		return false;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	/**
	 * Configura os mapeamento dos tipos de registro para classes POJO (Java Bean)
	 * 
	 */
	public abstract void setupTypeMapping();

	/**
	 * Retorna a URI da mensagem de entrada
	 * 
	 * @return String com a URI
	 */
	public abstract String getUri();
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void onStart() {
		
	}
	
	public void onEnd() {
		
	}
	
	public void execute(String userName, String codigoDistribuidor) {
		
		this.userName = userName;
		this.codigoDistribuidor = codigoDistribuidor;
		
		dynamicRouter.route(this);
	}
	
	/**
	 * Retorna o processador da mensagem
	 * 
	 * @return Implementacao de MessageProcessor
	 */
	public abstract MessageProcessor getMessageProcessor();
	
	public abstract RouteInterface getRouteInterface();

	/**
	 * @return the codigoDistribuidor
	 */
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	
}