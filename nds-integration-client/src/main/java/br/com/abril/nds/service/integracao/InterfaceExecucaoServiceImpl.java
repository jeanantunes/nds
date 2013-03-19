package br.com.abril.nds.service.integracao;

import org.lightcouch.NoDocumentException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.spring.NdsiRunner;
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
		String classe = PACOTE_PRIMEIRA_PARTE + classeExecucao.toLowerCase() + PACOTE_SEGUNDA_PARTE + classeExecucao + ROUTE;
		
		try {
		
			RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classe));
			route.execute(NdsiRunner.USER_NAME);
		
		} catch (NoDocumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum documento encontrado na base de dados!");
		}
		
		// br.com.abril.nds.integracao.ems0136.route.EMS0136Route

	}

}
