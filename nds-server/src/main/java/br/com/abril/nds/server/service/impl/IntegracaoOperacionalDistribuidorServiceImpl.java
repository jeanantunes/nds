package br.com.abril.nds.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.repository.IndicadorRepository;
import br.com.abril.nds.server.repository.OperacaoDistribuidorRepository;
import br.com.abril.nds.server.service.IntegracaoOperacionalDistribuidorService;
import br.com.abril.nds.util.CouchDBUtil;

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
	public List<OperacaoDistribuidor> obterInformacoesOperacionaisDistribuidores(Set<String> codigosDistribuidoresIntegracao) {
		
		List<OperacaoDistribuidor> informacoesOperacionaisDistribuidores = new ArrayList<OperacaoDistribuidor>();
		
		if (codigosDistribuidoresIntegracao == null || codigosDistribuidoresIntegracao.isEmpty()) {
			
			return informacoesOperacionaisDistribuidores;
		}
		
		for (String codigoDistribuidorIntegracao : codigosDistribuidoresIntegracao) {
			
			CouchDbClient couchDbClient = this.obterCouchDBClient(codigoDistribuidorIntegracao);
			
			OperacaoDistribuidor operacaoDistribuidor = null;
			
			try {
				
				operacaoDistribuidor = 
					couchDbClient.find(OperacaoDistribuidor.class, codigoDistribuidorIntegracao);
				
			} catch (NoDocumentException e) {
				
				continue;
			}
			
			informacoesOperacionaisDistribuidores.add(operacaoDistribuidor);
		}
		
		return informacoesOperacionaisDistribuidores;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<String> obterCodigosDistribuidoresIntegracao() {
		
		// TODO: Obter códigos (pendência Gabriel/Jonatas)
		Set<String> codigosDistribuidoresIntegracao = null;
		
		return codigosDistribuidoresIntegracao;
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
	
	/*
	 * Retorna o client para o CouchDB do banco de dados correspondente ao distribuidor.
	 */
	private CouchDbClient obterCouchDBClient(String codigoDistribuidorIntegracao) {
		
		return new CouchDbClient(
				CouchDBUtil.obterNomeBancoDeDadosIntegracaoDistribuidor(codigoDistribuidorIntegracao),
				true,
				this.couchDbProperties.getProtocol(),
				this.couchDbProperties.getHost(),
				this.couchDbProperties.getPort(),
				this.couchDbProperties.getUsername(),
				this.couchDbProperties.getPassword()
		);
	}

}
