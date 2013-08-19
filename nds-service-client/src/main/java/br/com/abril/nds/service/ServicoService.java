package br.com.abril.nds.service;

/**
 * Interface que define os métodos referentes aos seviços dos jornaleiros.
 * 
 * @author Discover Technology
 */
public interface ServicoService {

	/**
	 * Método que salva um serviço.
	 */
	void salvarServico();

	/**
	 * Método que remove um serviço.
	 */
	void removerServico();

	/**
	 * Método que busca serviços.
	 */
	void buscarServicos();

	/**
	 * Método que busca um serviço pelo ID.
	 * 
	 * @param id - ID do serviço.
	 */
	void buscaServicoPorID(Long id);
}
