package br.com.abril.nds.server.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.server.service.IntegracaoOperacionalDistribuidorService;
import br.com.abril.nds.util.CouchDBUtil;

public class IntegracaoOperacionalDistribuidorServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	@Autowired
	private IntegracaoOperacionalDistribuidorService integracaoOperacionalDistribuidorService;
	
	@Before
	public void setupCouchDB() {
		
		String[] idsDistribuidores = {"1", "2"};
		
		for (String idDistribuidor : idsDistribuidores) {
			
			CouchDbClient couchDbClient = this.obterCouchDBClient(idDistribuidor);
			
			OperacaoDistribuidor operacaoDistribuidor = couchDbClient.find(OperacaoDistribuidor.class, idDistribuidor);
			
			if (operacaoDistribuidor == null) {
			
				operacaoDistribuidor = new OperacaoDistribuidor();
				
				operacaoDistribuidor.setIdDistribuidorInterface(idDistribuidor);
				
				couchDbClient.save(operacaoDistribuidor);
			}
		}
	}
	
	@Test
	public void obterInformacoesOperacionaisDistribuidoresCodigosNulos() {
		
		Set<String> codigosDistribuidoresIntegracao = null;
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais =
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionaisDistribuidores(
				codigosDistribuidoresIntegracao);
		
		Assert.assertNotNull(listaInformacoesOperacionais);
		
		int tamanhoEsperadoLista = 0;
		
		Assert.assertEquals(tamanhoEsperadoLista, listaInformacoesOperacionais.size());
	}
	
	@Test
	public void obterInformacoesOperacionaisDistribuidoresSemCodigos() {
		
		Set<String> codigosDistribuidoresIntegracao = new HashSet<String>();
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais =
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionaisDistribuidores(
				codigosDistribuidoresIntegracao);
		
		Assert.assertNotNull(listaInformacoesOperacionais);
		
		int tamanhoEsperadoLista = 0;
		
		Assert.assertEquals(tamanhoEsperadoLista, listaInformacoesOperacionais.size());
	}
	
	@Test
	public void obterInformacoesOperacionaisDistribuidoresComCodigosInexistentes() {
		
		Set<String> codigosDistribuidoresIntegracao = new HashSet<String>();
		
		codigosDistribuidoresIntegracao.add("999");
		codigosDistribuidoresIntegracao.add("9999");
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais =
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionaisDistribuidores(
				codigosDistribuidoresIntegracao);
		
		Assert.assertNotNull(listaInformacoesOperacionais);
		
		int tamanhoEsperadoLista = 0;
		
		Assert.assertEquals(tamanhoEsperadoLista, listaInformacoesOperacionais.size());
	}
	
	@Test
	public void obterInformacoesOperacionaisDistribuidoresComCodigosExistentes() {
		
		Set<String> codigosDistribuidoresIntegracao = new TreeSet<String>();
		
		String idDistribuidorUm = "1";
		String idDistribuidorDois = "2";
		
		codigosDistribuidoresIntegracao.add(idDistribuidorUm);
		codigosDistribuidoresIntegracao.add(idDistribuidorDois);
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais =
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionaisDistribuidores(
				codigosDistribuidoresIntegracao);
		
		Assert.assertNotNull(listaInformacoesOperacionais);
		
		int tamanhoEsperadoLista = 2;
		
		Assert.assertEquals(tamanhoEsperadoLista, listaInformacoesOperacionais.size());
		
		Assert.assertEquals(
			listaInformacoesOperacionais.get(0).getIdDistribuidorInterface(), idDistribuidorUm);
		
		Assert.assertEquals(
			listaInformacoesOperacionais.get(1).getIdDistribuidorInterface(), idDistribuidorDois);
	}
	
	@Test
	public void obterCodigosDistribuidoresIntegracaoTest() {
		
		//TODO: Aguardando definição de pendência
	}
	
	@Test
	public void atualizarInformacoesOperacionaisDistribuidoresComDadosNulos() {
		
		this.integracaoOperacionalDistribuidorService.atualizarInformacoesOperacionaisDistribuidores(
			null);
	}
	
	@Test
	public void atualizarInformacoesOperacionaisDistribuidoresSemDados() {
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais = new ArrayList<OperacaoDistribuidor>();
		
		this.integracaoOperacionalDistribuidorService.atualizarInformacoesOperacionaisDistribuidores(
			listaInformacoesOperacionais);
	}
	
	@Test
	public void atualizarInformacoesOperacionaisDistribuidoresComDados() {
		
		List<OperacaoDistribuidor> listaInformacoesOperacionais = new ArrayList<OperacaoDistribuidor>();
		
		OperacaoDistribuidor operacaoDistribuidor = new OperacaoDistribuidor();
		
		operacaoDistribuidor.setIdDistribuidorInterface("1");
		
		List<Indicador> indicadores = new ArrayList<Indicador>();
		
		Indicador indicador = new Indicador();
		
		indicador.setId(1L);
		
		indicadores.add(indicador);
		
		operacaoDistribuidor.setIndicadores(indicadores);
		
		listaInformacoesOperacionais.add(operacaoDistribuidor);
		
		this.integracaoOperacionalDistribuidorService.atualizarInformacoesOperacionaisDistribuidores(
			listaInformacoesOperacionais);
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
