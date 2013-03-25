package br.com.abril.nds.service.integracao;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
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
	
	@Value("#{properties.interfacesProdin}")
	private String interfacesProdin;
	
	@Value("#{properties.interfacesMDCEntrada}")
	private String interfacesMDC;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarInterface(java.lang.String, java.lang.String)
	 */
	@Override
	public void executarInterface(String classeExecucao, Usuario usuario) throws BeansException, ClassNotFoundException {
		
		// Inclui o pacote na classe
		String classe = PACOTE_PRIMEIRA_PARTE + classeExecucao.toLowerCase() + PACOTE_SEGUNDA_PARTE + classeExecucao + ROUTE;
		
		try {
		
			RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classe));
			route.execute(usuario.getNome());
		
		} catch (NoDocumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum documento encontrado na base de dados!");
		} catch(Exception e) {
			throw e;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarTodasInterfacesEmOrdem(java.lang.String)
	 */
	@Override
	public void executarTodasInterfacesEmOrdem(Usuario usuario) throws BeansException, ClassNotFoundException {
		
		String[] interfacesProdinReprocessar = interfacesProdin.split(",");
		
		String[] interfacesMDCReprocessar = interfacesMDC.split(",");
		
		for(String interfaceProdin : interfacesProdinReprocessar) {
			
			String classeExecucao = "EMS"+ StringUtils.leftPad(interfaceProdin.trim(), 4, '0');
			
			try {
				this.executarInterface(classeExecucao, usuario);
			} catch (ValidacaoException ve) {
				
			}
		}
		
		for(String interfaceMDC : interfacesMDCReprocessar) {
			
			String classeExecucao = "EMS"+ StringUtils.leftPad(interfaceMDC, 4, '0');
			
			try {
				this.executarInterface(classeExecucao, usuario);
			} catch (ValidacaoException ve) {
				
			}
		}
				
	}

}
