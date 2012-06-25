package br.com.abril.nds.service;

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
	 */
	public void executarInterface(String classeExecucao, Usuario usuario);
	
}
