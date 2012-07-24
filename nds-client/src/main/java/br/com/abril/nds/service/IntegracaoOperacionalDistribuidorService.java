package br.com.abril.nds.service;

import br.com.abril.nds.server.model.OperacaoDistribuidor;


/**
 * Interface de serviço que define métodos de
 * integração operacional do distribuidor.
 * 
 * @author Discover Technology
 *
 */
public interface IntegracaoOperacionalDistribuidorService {

	/**
	 * Obtém as inforamções operacionais do distribuidor.
	 * 
	 * @return {@link OperacaoDistribuidor}
	 */
	OperacaoDistribuidor obterInformacoesOperacionais();
	
	/**
	 * Efetua a integração das informações operacionais do distribuidor.
	 */
	void integrarInformacoesOperacionais(OperacaoDistribuidor operacaoDistribuidor);
	
}
