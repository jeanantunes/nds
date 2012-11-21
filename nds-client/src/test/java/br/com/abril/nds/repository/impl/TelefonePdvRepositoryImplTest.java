package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.repository.TelefonePdvRepository;

public class TelefonePdvRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	TelefonePdvRepository telefonePdvRepository;
	
	@Test
	public void removerTelefonesPdv(){
		Collection<Long> listaTelefones = new HashSet<Long>();
		listaTelefones.add(1L);
		listaTelefones.add(2L);
		
		telefonePdvRepository.removerTelefonesPdv(listaTelefones);
	}
	
	@Test
	public void pesquisarTelefonePrincipalPdv(){
		Long idPdv = 1L;
		
		Telefone telefone =  telefonePdvRepository.pesquisarTelefonePrincipalPdv(idPdv);
	}
	
	@Test
	public void buscarTelefonesPdvIdPdv(){
		Long idPdv = 1L;
		
		List<TelefoneAssociacaoDTO> telefoneAssociacaoDTOs = 
				telefonePdvRepository.buscarTelefonesPdv(idPdv, null);
		
		Assert.assertNotNull(telefoneAssociacaoDTOs);
	}
	
	@Test
	public void buscarTelefonesPdvIdsIgnorar(){
		Set <Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		List<TelefoneAssociacaoDTO> telefoneAssociacaoDTOs = 
				telefonePdvRepository.buscarTelefonesPdv(null, idsIgnorar);
		
		Assert.assertNotNull(telefoneAssociacaoDTOs);
	}
	

	@Test
	public void buscarTelefonesPessoaPorPdv(){
		Long idPdv = 1L;
		
		List<Telefone> telefone = telefonePdvRepository.buscarTelefonesPessoaPorPdv(idPdv);
		
		Assert.assertNotNull(telefone);
	}
	
	@Test
	public void obterTelefonePorTelefonePdvIdTelefone(){
		Long idTelefone = 1L;
		
		TelefonePDV telefonePdv = telefonePdvRepository.obterTelefonePorTelefonePdv(idTelefone, null);
	
	}
	
	@Test
	public void obterTelefonePorTelefonePdvIdPdv(){
		Long idPdv = 1L;
		
		TelefonePDV telefonePdv = telefonePdvRepository.obterTelefonePorTelefonePdv(null, idPdv);
	
	}
	
	@Test
	public void excluirTelefonesPdv(){
		Long idPdv = 1L;
		
		telefonePdvRepository.excluirTelefonesPdv(idPdv);
	
	}
}
