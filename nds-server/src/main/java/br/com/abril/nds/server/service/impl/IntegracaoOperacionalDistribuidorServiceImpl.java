package br.com.abril.nds.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.Distribuidor;
import br.com.abril.nds.server.service.IntegracaoOperacionalDistribuidorService;

/**
 * Implementação do serviço de integração operacional dos distribuidores.
 * 
 * @author Discover Technology
 *
 */
public class IntegracaoOperacionalDistribuidorServiceImpl implements IntegracaoOperacionalDistribuidorService {

	@Autowired
	private CouchDbProperties couchDbProperties;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Distribuidor> obterInformacoesOperacionaisDistribuidores() {
		
		/* TODO: Obter dados pelo de todos distribuidores pelo CouchDB
		 * 
		 * Haverá um database para cada distribuidor.
		 * 
		 * Então será necessário instanciar um cliente do CouchDB para
		 * cada um e obter os dados.
		 * 
		 */
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void atualizarInformacoesOperacionaisDistribuidores(
			List<Distribuidor> listaInformacoesOperacionaisDistribuidores) {
		
		//TODO: Atualizar dados recebidos na base de dados (MYSQL) do DServer
	}

}
