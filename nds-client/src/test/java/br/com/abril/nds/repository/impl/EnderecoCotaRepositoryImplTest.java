package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;

public class EnderecoCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EnderecoCotaRepositoryImpl enderecoCotaRepositoryImpl;
	
	@Test
	public void testarRemoverEnderecoCota() {
		
		Endereco endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCodigoBairro("1");
		endereco.setBairro("testeBairro");
		
		Long idCota = 1L;
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		listaEndereco.add(endereco);
		
		
		enderecoCotaRepositoryImpl.removerEnderecosCota(idCota, listaEndereco);
		
//		Assert.assertNotNull(object);
		
	}
	
	@Test
	public void testarGetPrincipal() {
		
		EnderecoCota enderecoCota;
		
		Long idCota = 1L;
		
		enderecoCota = enderecoCotaRepositoryImpl.getPrincipal(idCota);
		
//		Assert.assertNull(enderecoCota);
		
	}
	
	@Test
	public void testarObterEnderecoPorTipoEndereco() {
		
		EnderecoCota enderecoCota;
		
		Long idCota = 1L;
		
		enderecoCota = enderecoCotaRepositoryImpl.obterEnderecoPorTipoEndereco(idCota, TipoEndereco.COBRANCA);
		
//		Assert.assertNull(enderecoCota);
		
	}

}
