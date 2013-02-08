package br.com.abril.nds.service.integracao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.InterfaceExecucaoService;

/**
 * Implementação do serviço de execução de interfaces
 * @author InfoA2
 */
@Service
public class InterfaceExecucaoServiceImpl implements InterfaceExecucaoService {

	@Autowired
	private ApplicationContext applicationContext;
	
	private static final String PACOTE_PRIMEIRA_PARTE = "br.com.abril.nds.integracao.";
	private static final String PACOTE_SEGUNDA_PARTE = ".route.";
	private static final String ROUTE = "Route";
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarInterface(java.lang.String, java.lang.String)
	 */
	@Override
	public void executarInterface(String classeExecucao, Usuario usuario) throws BeansException, ClassNotFoundException {
		
		// Inclui o pacote na classe
		String classe = PACOTE_PRIMEIRA_PARTE + classeExecucao.substring(0, classeExecucao.indexOf(ROUTE)).toLowerCase() + PACOTE_SEGUNDA_PARTE + classeExecucao;
		
		RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classe));
		route.execute(usuario.getLogin());
		
		Class.forName("br.com.abril.nds.integracao.ems0106.route.EMS0106Route");
		Class.forName("java.lang.Thread");
	}

}
