package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.server.model.DistribuidorServer;
import br.com.abril.nds.service.IntegracaoService;
import br.com.abril.nds.service.IntegracaoOperacionalDistribuidorService;

/**
 * Implementação do serviço de integração operacional do distribuidor.
 * 
 * @author Discover Technology
 *
 */
@Service
public class IntegracaoOperacionalDistribuidorServiceImpl implements IntegracaoOperacionalDistribuidorService {

	@Autowired
	private IntegracaoService integracaoService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void integrarInformacoesOperacionais(DistribuidorServer distribuidorServer) {
		
		this.integracaoService.getCouchDbClient().save(distribuidorServer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public DistribuidorServer obterInformacoesOperacionais() {
		
		//TODO: Obter indicadores
		
		return new DistribuidorServer();
	}

}
