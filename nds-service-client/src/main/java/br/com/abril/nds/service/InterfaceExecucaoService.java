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
	 * @param classeExecucao
	 * @param usuario
	 * @throws ClassNotFoundException 
	 * @throws BeansException 
	 */
	public void executarInterface(String classeExecucao, Usuario usuario) throws BeansException, ClassNotFoundException;
	
	/**
	 * Executa as Interfaces em Ordem de Prioridade 
	 * @param usuario
	 * @throws BeansException
	 * @throws ClassNotFoundException
	 */
	public void executarTodasInterfacesEmOrdem(Usuario usuario) throws BeansException, ClassNotFoundException;
	
}
