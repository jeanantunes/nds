package br.com.abril.nds.service.integracao;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceExecucaoServiceImpl.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private static final String PACOTE_PRIMEIRA_PARTE = "br.com.abril.nds.integracao.";
	
	private static final String PACOTE_SEGUNDA_PARTE = ".route.";
	
	private static final String ROUTE = "Route";
	
	@Value("${interfacesProdin:}")
	private String interfacesProdin;
	
	@Value("${interfacesMDCEntrada:}")
	private String interfacesMDC;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarInterface(java.lang.String, br.com.abril.nds.model.seguranca.Usuario, java.lang.String)
	 */
	@Override
	public void executarInterface(String idInterface, Usuario usuario, String codigoDistribuidor) throws BeansException, ClassNotFoundException {

		String nome = getClasseExecucao(idInterface).get();
		
		// Inclui o pacote na classe
		String classe = PACOTE_PRIMEIRA_PARTE + nome.toLowerCase() + PACOTE_SEGUNDA_PARTE + nome + ROUTE;
		
		try {
		
			RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(classe));
			route.execute(usuario.getNome(), codigoDistribuidor);
		
		} catch (NoDocumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum documento encontrado na base de dados!");

		} catch(Exception e) {
			LOGGER.error("Erro ao executar interface: "+ nome, e);
			throw e;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarTodasInterfacesEmOrdem(br.com.abril.nds.model.seguranca.Usuario, java.lang.String)
	 */
	@Override
	public void executarTodasInterfacesProdinEmOrdem(Usuario usuario, String codigoDistribuidor) throws BeansException, ClassNotFoundException {
		
		String[] interfacesProdinReprocessar = interfacesProdin.split(",");
		
		for (String interfaceProdin : interfacesProdinReprocessar) {
			
			try {
				this.executarInterface(interfaceProdin, usuario, codigoDistribuidor);
			} catch (ValidacaoException ve) {
				LOGGER.error("Erro ao executar interface: "+ interfaceProdin, ve);
			} catch(Exception e) {
				LOGGER.error("Erro ao executar interface: "+ interfaceProdin, e);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.InterfaceExecucaoService#executarTodasInterfacesFCEmOrdem(br.com.abril.nds.model.seguranca.Usuario)
	 */
	@Override
	public void executarTodasInterfacesMDCEmOrdem(Usuario usuario) throws BeansException, ClassNotFoundException {
		
		String[] interfacesMDCReprocessar = interfacesMDC.split(",");
		
		for (String interfaceMDC : interfacesMDCReprocessar) {
			
			try {
				this.executarInterface(interfaceMDC, usuario, null);
			} catch (ValidacaoException ve) {
				LOGGER.error("Erro ao executar interface: "+ interfaceMDC, ve);
			} catch(Exception e) {
				LOGGER.error("Erro ao executar interface: "+ interfaceMDC, e);
			}
		}
	}
	
	private WeakReference<String> getClasseExecucao(String interfaceExecucao) {
		
		return new WeakReference<String>(
				new StringBuilder("EMS")
				.append(StringUtils.leftPad(interfaceExecucao.trim(), 4, '0')).toString());
	}
	
	public boolean isInterfaceProdin(String idInterface) {
		
		String[] interfacesProdinReprocessar = interfacesProdin.split(",");
		
		return Arrays.asList(interfacesProdinReprocessar).contains(idInterface);
	}
	
	public boolean isInterfaceMDC(String idInterface) {
		
		String[] interfacesMDCReprocessar = interfacesMDC.split(",");
		
		return Arrays.asList(interfacesMDCReprocessar).contains(idInterface);
	}

}
