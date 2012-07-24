package br.com.abril.nds.server.service;

import java.util.List;

import br.com.abril.nds.server.model.Distribuidor;

/**
 * Interface de serviço que define métodos de
 * integração operacional dos distribuidores.
 * 
 * @author Discover Technology
 *
 */
public interface IntegracaoOperacionalDistribuidorService {
	
	/**
	 * Obtém as informações operacionais mais recentes dos distribuidores.
	 * 
	 * @return {@link List} de informações operacionais dos distribuidores
	 */
	List<Distribuidor> obterInformacoesOperacionaisDistribuidores();
	
	/**
	 * Atualiza a base de dados com as informações operacionais
	 * dos distribuidores vindas da integração.
	 * 
	 * @param listaInformacoesOperacionaisDistribuidores - lista de informações operacionais dos distribuidores
	 */
	void atualizarInformacoesOperacionaisDistribuidores(
		List<Distribuidor> listaInformacoesOperacionaisDistribuidores);
	
}
