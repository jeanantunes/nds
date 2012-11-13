package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.vo.EnderecoVO;

public class EnderecoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	@Test
	public void removerEnderecos(){
		List<Long> idsEndereco = new ArrayList<Long>();
		idsEndereco.add(1L);
		
		enderecoRepository.removerEnderecos(idsEndereco);
	}
	
	@Test
	public void buscarEnderecosPessoaIdPessoa(){
		Long idPessoa = 1L;
		
		List<Endereco> endereco = enderecoRepository.buscarEnderecosPessoa(idPessoa, null);
		
		Assert.assertNotNull(endereco);
	}
	
	@Test
	public void buscarEnderecosPessoaIdsIgnorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		List<Endereco> endereco = enderecoRepository.buscarEnderecosPessoa(null, idsIgnorar);
		
		Assert.assertNotNull(endereco);
	}
	
	@Test
	public void obterMunicipiosCotas(){
				
		List<String> string = enderecoRepository.obterMunicipiosCotas();
		
		Assert.assertNotNull(string);
	}
	
	
	@Test
	public void obterLocalidadesPorUFNomeNome(){
		String nome = "";
				
		List<Localidade> localidade = enderecoRepository.obterLocalidadesPorUFNome(nome, null);
		
		Assert.assertNotNull(localidade);
	}
	
	@Test
	public void obterLocalidadesPorUFNomeSiglaUF(){
		String siglaUF = "SP";
				
		List<Localidade> localidade = enderecoRepository.obterLocalidadesPorUFNome(null, siglaUF);
		
		Assert.assertNotNull(localidade);
	}
	
	@Test
	public void obterEnderecoPorCep(){
		String cep = "12345-000";
				
		EnderecoVO enderecoVO = enderecoRepository.obterEnderecoPorCep(cep);
		
	}
	
	@Test
	public void obterBairrosPorCodigoIBGENomeNome(){
		String nome = "bairroTeste";
				
		List<Bairro> bairro = enderecoRepository.obterBairrosPorCodigoIBGENome(nome, null);
		
		Assert.assertNotNull(bairro);
	}
	
	@Test
	public void obterBairrosPorCodigoIBGENomeCodigoIBGE(){
		Long codigoIBGE = 1234567L ;
				
		List<Bairro> bairro = enderecoRepository.obterBairrosPorCodigoIBGENome(null, codigoIBGE);
		
		Assert.assertNotNull(bairro);
	}
	
	@Test
	public void obterLogradourosPorCodigoBairroNomeCodBairro(){
		Long codBairro = 1L ;
				
		List<Logradouro> logradouro = 
				enderecoRepository.obterLogradourosPorCodigoBairroNome(codBairro, null);
		
		Assert.assertNotNull(logradouro);
	}
	
	@Test
	public void obterLogradourosPorCodigoBairroNomeNomeLogradouro(){
		String nomeLogradouro = "logradouroTeste";
				
		List<Logradouro> logradouro = 
				enderecoRepository.obterLogradourosPorCodigoBairroNome(null, nomeLogradouro);
		
		Assert.assertNotNull(logradouro);
	}

}
