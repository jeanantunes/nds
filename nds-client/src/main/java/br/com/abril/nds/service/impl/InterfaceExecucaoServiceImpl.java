package br.com.abril.nds.service.impl;

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
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarInterface(java.lang.String, java.lang.String)
	 */
	@Override
	public void executarInterface(String classeExecucao, Usuario usuario) throws BeansException, ClassNotFoundException {
		RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classeExecucao));
		route.execute(usuario.getLogin());
	}

}
