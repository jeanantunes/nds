package br.com.abril.nds.server.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.server.model.OperacaoDistribuidor;

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
	 * @param codigosDistribuidoresIntegracao - códigos dos distribuidores para integração
	 * 
	 * @return {@link List} de informações operacionais dos distribuidores
	 */
	List<OperacaoDistribuidor> obterInformacoesOperacionaisDistribuidores(Set<String> codigosDistribuidoresIntegracao);
	
	/**
	 * Obtém os códigos dos distribuidores possíveis de integração.
	 * 
	 * @return {@link Set} de códigos dos distribuidores para integração
	 */
	Set<String> obterCodigosDistribuidoresIntegracao();
	
	/**
	 * Atualiza a base de dados com as informações operacionais
	 * dos distribuidores vindas da integração.
	 * 
	 * @param listaInformacoesOperacionaisDistribuidores - lista de informações operacionais dos distribuidores
	 */
	void atualizarInformacoesOperacionaisDistribuidores(
		List<OperacaoDistribuidor> listaInformacoesOperacionaisDistribuidores);
	
}
