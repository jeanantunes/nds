package br.com.abril.nds.service.impl;

import org.springframework.stereotype.Service;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.InterfaceExecucaoService;

/**
 * Implementação do serviço de execução de interfaces
 * @author InfoA2
 */
@Service
public class InterfaceExecucaoServiceImpl implements InterfaceExecucaoService {

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarInterface(java.lang.String, java.lang.String)
	 */
	@Override
	public void executarInterface(String classeExecucao, Usuario usuario) {
		/*RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classeExecucao));
		route.execute(usuario.getLogin());*/
	}

}
