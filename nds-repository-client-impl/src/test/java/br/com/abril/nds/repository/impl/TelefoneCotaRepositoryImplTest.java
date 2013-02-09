package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.repository.TelefoneCotaRepository;

public class TelefoneCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Test
	public void buscarTelefonesCotaTest(){
		
		this.telefoneCotaRepository.buscarTelefonesCota(1L, null);
		
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneCotaRepository.buscarTelefonesCota(1L, set);
		
		}
		
	@Test
	public void removerTelefonesCotaTest(){
		
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneCotaRepository.removerTelefonesCota(set);
	}
	
	@Test
	public void buscarTelefonesPessoaPorCota(){
		
		Long idCota = 1L;
		
		List<Telefone> telefones = telefoneCotaRepository.buscarTelefonesPessoaPorCota(idCota);
		
		Assert.assertNotNull(telefones);
	}
	
	@Test
	public void obterTelefonePrincipal(){
		
		Long idCota = 1L;
		
		TelefoneCota telefoneCota =  telefoneCotaRepository.obterTelefonePrincipal(idCota);
		
	
	}
}
