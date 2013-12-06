package br.com.abril.nds.service;

import org.springframework.beans.BeansException;

import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface de execução de interfaces
 * @author InfoA2
 */
public interface InterfaceExecucaoService {

	/**
	 * Executa Interfaces
	 * @param idInterface
	 * @param usuario
	 * @param codigoDistribuidor
	 * @throws ClassNotFoundException 
	 * @throws BeansException 
	 */
	void executarInterface(String idInterface, Usuario usuario, String codigoDistribuidor) throws BeansException, ClassNotFoundException;
	
	/**
	 * Executa as Interfaces PRODIN em Ordem de Prioridade 
	 * @param usuario
	 * @param codigoDistribuidor
	 * @throws BeansException
	 * @throws ClassNotFoundException
	 */
	void executarTodasInterfacesProdinEmOrdem(Usuario usuario, String codigoDistribuidor) throws BeansException, ClassNotFoundException;
	
	/**
	 * Executa as Interfaces MDC em Ordem de Prioridade 
	 * @param usuario
	 * @throws BeansException
	 * @throws ClassNotFoundException
	 */
	void executarTodasInterfacesMDCEmOrdem(Usuario usuario) throws BeansException, ClassNotFoundException;
	
	boolean isInterfaceProdin(String idInterface);
	
	boolean isInterfaceMDC(String idInterface);
	
}
