package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.PessoaFisicaRepository;



public class PessoaFisicaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@SuppressWarnings("unused")
	@Test
	public void buscarPorCpf(){		
		PessoaFisica pessoaFisica = pessoaFisicaRepository.buscarPorCpf("123.456.789-98");
	}

}
