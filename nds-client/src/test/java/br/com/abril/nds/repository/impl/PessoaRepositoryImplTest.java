package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class PessoaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PessoaRepositoryImpl pessoaRepository;

	
	@Test
	@Ignore
	public void salvarPessoaFisica() {
		PessoaFisica pf = Fixture.pessoaFisica("123.456.789-00",
				"jose.silva@mail.com", "José da Silva");
		pessoaRepository.adicionar(pf);
	}
	
	@Test
	public void salvarPessoaJuridica() {
		PessoaJuridica pj = Fixture.pessoaJuridica("ACME CORP",
				"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
		pessoaRepository.adicionar(pj);
	}
	
	@Test
	@Ignore
	public void buscarPorNome() {
		PessoaFisica pf1 = Fixture.pessoaFisica("123.456.789-00",
				"jose.silva@mail.com", "José da Silva");
		
		PessoaFisica pf2 = Fixture.pessoaFisica("321.654.987-00",
				"joao.silva@mail.com", "João da Silva");

		
		PessoaJuridica pj1 = Fixture.pessoaJuridica("José Ltda",
				"00.000.000/0001-00", "000.000.000.000", "joseltda@mail.com");

		
		PessoaJuridica pj2 = Fixture.pessoaJuridica("Acme Ltda",
				"11.111.111/0001-11", "000.000.000.000", "acme@mail.com");
		getSession().save(pf1);
		getSession().save(pf2);
		getSession().save(pj1);
		getSession().save(pj2);
		flushClear();
		
		List<Pessoa> pessoas = pessoaRepository.buscarPorNome("José"); 
		Assert.assertTrue(pessoas.size() == 2);
		Assert.assertTrue(pessoas.contains(pf1));
		Assert.assertTrue(pessoas.contains(pj1));
	}

}
