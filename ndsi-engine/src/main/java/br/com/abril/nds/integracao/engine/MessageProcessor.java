package br.com.abril.nds.integracao.engine;

import br.com.abril.nds.integracao.engine.data.Message;

public interface MessageProcessor {
	
	/**
	 * Método a ser chamado para realizar as ações de pré-processamento (antes 
	 * do processamento principal).
	 */
	void preProcess();
	
	/**
	 * Método a ser chamado para realizar as ações de pré-processamento (antes 
	 * do processamento principal).
	 * 
	 * @param message
	 */
	void preProcess(Message message);
	
	public void processMessage(Message message);
	
	/**
	 * Método a ser chamado para realizar as ações de pós-processamento (após
	 * o processamento principal).
	 */
	void posProcess();
	
	/**
	 * Método a ser chamado para realizar as ações de pós-processamento (após
	 * o processamento principal).
	 * 
	 * @param message
	 */
	void posProcess(Message message);
}
