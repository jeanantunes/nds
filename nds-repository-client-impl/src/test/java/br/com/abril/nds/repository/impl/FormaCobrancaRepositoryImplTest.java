package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public class FormaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private FormaCobrancaRepositoryImpl formaCobrancaRepositoryImpl;
	
	@Test
	public void testarObterPorTipoEBanco() {
		
		FormaCobranca formaCobranca;
		
		TipoCobranca tipo = TipoCobranca.BOLETO;
		Banco banco = new Banco();
		banco.setId(1L);
		
		formaCobranca = formaCobrancaRepositoryImpl.obterPorTipoEBanco(tipo, banco);
		
		Assert.assertNull(formaCobranca);
		
	}
	
	@Test
	public void testarObterBancosPorTipoDeCobranca() {
		
		List<Banco> listaBanco;
		
		listaBanco = formaCobrancaRepositoryImpl.obterBancosPorTipoDeCobranca(TipoCobranca.BOLETO);
		
		Assert.assertNotNull(listaBanco);
	}
	
	@Test
	public void testarObterFormaCobrancaPrincipalCota() {
		
		FormaCobranca formaCobranca;
		
		Long idCota = 1L;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobrancaPrincipalCota(idCota);
		
		Assert.assertNull(formaCobranca);
		
	}
	
	@Test
	public void testarObterFormasCobrancaCota() {
		
		List<FormaCobranca> listaFormaCobranca;
		
		Cota cota = new Cota();
		cota.setId(1L);
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterFormasCobrancaCota(cota);
		
		Assert.assertNotNull(listaFormaCobranca);
		
	}
	
	@Test
	public void testarObterQuantidadeFormasCobrancaCota() {
		
		int quantidadeFormas;
		
		Cota cota = new Cota();
		cota.setId(1L);
		
		quantidadeFormas = formaCobrancaRepositoryImpl.obterQuantidadeFormasCobrancaCota(cota);
		
		Assert.assertNotNull(quantidadeFormas);
		
	}
	
	@Test
	public void testarDesativarFormaCobranca() {
		
		long idFormaCobranca = 1L;
		
		formaCobrancaRepositoryImpl.desativarFormaCobranca(idFormaCobranca);
		
//		Assert.assertNotNull()
		
	}
	
	@Test
	public void testarObterPorCotaETipoCobranca() {
		
		List<FormaCobranca> listaFormaCobranca;
		
		Long idCota = 1L;
		Long idFormaCobranca = 2L;
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterPorCotaETipoCobranca(idCota, TipoCobranca.BOLETO, idFormaCobranca);
		
		Assert.assertNotNull(listaFormaCobranca);
			
	}
	
	@Test
	public void testarObterPorDistribuidorETipoCobranca() {
		
		List<FormaCobranca> listaFormaCobranca;
		
		Long idDistribuidor = 1L;
		Long idFormaCobranca= 2L;
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterPorDistribuidorETipoCobranca(idDistribuidor, TipoCobranca.BOLETO, idFormaCobranca);
		
		Assert.assertNotNull(listaFormaCobranca);
		
	}
	
	
	
	
	
	
	

}
