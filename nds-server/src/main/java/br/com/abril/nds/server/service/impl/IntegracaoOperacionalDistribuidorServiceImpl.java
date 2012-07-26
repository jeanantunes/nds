package br.com.abril.nds.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.repository.IndicadorRepository;
import br.com.abril.nds.server.repository.OperacaoDistribuidorRepository;
import br.com.abril.nds.server.service.IntegracaoOperacionalDistribuidorService;

/**
 * Implementação do serviço de integração operacional dos distribuidores.
 * 
 * @author Discover Technology
 *
 */
@Service
public class IntegracaoOperacionalDistribuidorServiceImpl implements IntegracaoOperacionalDistribuidorService {

	@Autowired
	private CouchDbProperties couchDbProperties;
	
	@Autowired
	private OperacaoDistribuidorRepository operacaoDistribuidorRepository;
	
	@Autowired
	private IndicadorRepository indicadorRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<OperacaoDistribuidor> obterInformacoesOperacionaisDistribuidores() {
		
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
			List<OperacaoDistribuidor> listaInformacoesOperacionaisDistribuidores) {
		
		if (listaInformacoesOperacionaisDistribuidores != null){
			
			for (OperacaoDistribuidor operacaoDistribuidor : listaInformacoesOperacionaisDistribuidores){
				
				if (operacaoDistribuidor != null){
					
					this.operacaoDistribuidorRepository.alterar(operacaoDistribuidor);
				}
				
				if (operacaoDistribuidor.getIndicadores() != null){
					
					for (Indicador indicador : operacaoDistribuidor.getIndicadores()){
						
						if (indicador != null){
							
							this.indicadorRepository.alterar(indicador);
						}
					}
				}
			}
		}
	}

}