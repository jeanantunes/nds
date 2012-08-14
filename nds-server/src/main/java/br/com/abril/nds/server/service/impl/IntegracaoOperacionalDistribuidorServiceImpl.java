package br.com.abril.nds.server.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.CouchDBUser;
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
			
			String nomeBancoDeDados = 
				CouchDBUtil.obterNomeBancoDeDadosIntegracaoDistribuidor(codigoDistribuidorIntegracao);
			
			CouchDbClient couchDbClient = this.obterCouchDBClient(nomeBancoDeDados);
			
			couchDbClient.setGsonBuilder(CouchDBUtil.getGsonBuilderForDate());
			
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
		
		CouchDbClient couchDbClient = this.obterCouchDBClient(CouchDBUtil.DB_NAME_USERS);

		View view = couchDbClient.view("users/distribuidores");
		
		ViewResult<String, CouchDBUser, Void> viewResult = view.queryView(String.class, CouchDBUser.class, Void.class);
		
		Set<String> codigosDistribuidoresIntegracao = new HashSet<String>();
		
		for (ViewResult<String, CouchDBUser, Void>.Rows row : viewResult.getRows()) {
			
			codigosDistribuidoresIntegracao.add(row.getValue().getIdDistribuidor());
		}
		
		return codigosDistribuidoresIntegracao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void atualizarInformacoesOperacionaisDistribuidores(
			List<OperacaoDistribuidor> listaInformacoesOperacionaisDistribuidores) {
		
		if (listaInformacoesOperacionaisDistribuidores != null) {
			
			for (OperacaoDistribuidor operacaoDistribuidor : listaInformacoesOperacionaisDistribuidores) {
				
				if (operacaoDistribuidor.getIdDistribuidorInterface() == null) {
					
					continue;
				}
				
				OperacaoDistribuidor operacaoDistribuidorAtual =
					this.operacaoDistribuidorRepository.buscarPorId(
						operacaoDistribuidor.getIdDistribuidorInterface());

				if (operacaoDistribuidorAtual == null) {
					
					this.operacaoDistribuidorRepository.adicionar(operacaoDistribuidor);
					
					operacaoDistribuidorAtual =
						this.operacaoDistribuidorRepository.buscarPorId(
							operacaoDistribuidor.getIdDistribuidorInterface());
					
				} else {
				
					operacaoDistribuidorAtual.setRevisao(operacaoDistribuidor.getRevisao());
					operacaoDistribuidorAtual.setDataOperacao(operacaoDistribuidor.getDataOperacao());
					operacaoDistribuidorAtual.setStatusOperacao(operacaoDistribuidor.getStatusOperacao());
					
					this.operacaoDistribuidorRepository.alterar(operacaoDistribuidorAtual);
				}
				
				if (operacaoDistribuidor.getIndicadores() != null) {
					
					for (Indicador indicador : operacaoDistribuidor.getIndicadores()) {
						
						if (indicador != null) {
							
							indicador.setDistribuidor(operacaoDistribuidorAtual);
							
							if (indicador.getId() == null) {
							
								this.indicadorRepository.adicionar(indicador);
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * Retorna o client para o CouchDB de acordo com o nome do bancos de dados.
	 */
	private CouchDbClient obterCouchDBClient(String nomeBancoDeDados) {
		
		return new CouchDbClient(
				nomeBancoDeDados,
				true,
				this.couchDbProperties.getProtocol(),
				this.couchDbProperties.getHost(),
				this.couchDbProperties.getPort(),
				this.couchDbProperties.getUsername(),
				this.couchDbProperties.getPassword()
		);
	}

}
