package br.com.abril.nds.service.impl;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.service.IntegracaoOperacionalDistribuidorService;

/**
 * Implementação do serviço de integração operacional do distribuidor.
 * 
 * @author Discover Technology
 *
 */
@Service
public class IntegracaoOperacionalDistribuidorServiceImpl implements IntegracaoOperacionalDistribuidorService {
	
	private static final String DB_NAME = "db_integracao";
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClientIntegracao;
	
	@PostConstruct
	public void initCouchDbClient() {
		
		this.couchDbClientIntegracao = 
			new CouchDbClient(
				DB_NAME, true, couchDbProperties.getProtocol(), couchDbProperties.getHost(), 
					couchDbProperties.getPort(), couchDbProperties.getUsername(), 
						couchDbProperties.getPassword());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void integrarInformacoesOperacionais(OperacaoDistribuidor operacaoDistribuidor) {
		
		this.couchDbClientIntegracao.save(operacaoDistribuidor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public OperacaoDistribuidor obterInformacoesOperacionais() {
		
		//TODO: Obter indicadores
		
		return new OperacaoDistribuidor();
	}

}
