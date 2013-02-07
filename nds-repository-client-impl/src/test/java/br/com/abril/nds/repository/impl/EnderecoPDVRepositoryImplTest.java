package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;

public class EnderecoPDVRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EnderecoPDVRepositoryImpl enderecoPDVRepositoryImpl;
	
	@Test
	public void testarBuscaEnderecosPDV() {
		
		List<EnderecoAssociacaoDTO> buscaEnderecos;
		
		Long idPDV = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();		
		
		buscaEnderecos = enderecoPDVRepositoryImpl.buscaEnderecosPDV(idPDV, idsIgnorar);
		
		Assert.assertNotNull(buscaEnderecos);
		
	}
	
	@Test
	public void testarBuscaEnderecosPDVIdsIgnorar() {
		
		List<EnderecoAssociacaoDTO> buscaEnderecos;
		
		Long idPDV = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		
		buscaEnderecos = enderecoPDVRepositoryImpl.buscaEnderecosPDV(idPDV, idsIgnorar);
		
		Assert.assertNotNull(buscaEnderecos);
		
	}
	
	@Test
	public void testarBuscarEnderecosPessoaPorPDV() {
		
		List<Endereco> buscaEnderecosPessoal;
		
		Long idPDV = 1L;
		
		buscaEnderecosPessoal = enderecoPDVRepositoryImpl.buscarEnderecosPessoaPorPDV(idPDV);
		
		Assert.assertNotNull(buscaEnderecosPessoal);
		
	}
	
	@Test
	public void testarBuscarEnderecoPorEnderecoPDV() {
		
		EnderecoPDV enderecoPDV;
		
		Long idEndereco = 1L;
		Long idPDV = 2L;
		
		enderecoPDV = enderecoPDVRepositoryImpl.buscarEnderecoPorEnderecoPDV(idEndereco, idPDV);
		
		Assert.assertNull(enderecoPDV);
		
	}
	
	@Test
	public void testarExcluirEnderecosPorIdPDV() {
		
		Long idPDV = 1L;
		
		enderecoPDVRepositoryImpl.excluirEnderecosPorIdPDV(idPDV);
		
//		Assert.assertNotNull()
		
	}
	
	@Test
	public void testarExcluirEnderecosPDV () {
		
		Collection<Long> idsEnderecoPDV = new  ArrayList<Long>();
		idsEnderecoPDV.add(1L);
		idsEnderecoPDV.add(2L);
		
		enderecoPDVRepositoryImpl.excluirEnderecosPDV(idsEnderecoPDV);
		
//		Assert.assertNotNull()
		
	}
	
	@Test
	public void testarBuscarMunicipioPDVPrincipal() {
		
		List<ItemDTO<Integer,String>> buscaMunicipio;
		
		buscaMunicipio = enderecoPDVRepositoryImpl.buscarMunicipioPdvPrincipal();
		
		Assert.assertNotNull(buscaMunicipio);
		
	}
	
	@Test
	public void testarBuscarMunicipioPDVPrincipalEndereco() {
		
		Endereco endereco;
		
		Integer codigoCidadeIBGE = 1;
		
		endereco = enderecoPDVRepositoryImpl.buscarMunicipioPdvPrincipal(codigoCidadeIBGE);
		
		Assert.assertNull(endereco);
		
		
	}
	
	@Test
	public void testarBuscarEnderecoPrincipal() {
		
		Endereco endereco;
		
		Long idPdv = 1L;
		
		endereco = enderecoPDVRepositoryImpl.buscarEnderecoPrincipal(idPdv);
		
		Assert.assertNull(endereco);
	}

	
}

