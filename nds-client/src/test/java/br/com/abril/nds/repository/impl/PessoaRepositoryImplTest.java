package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.Pessoa;
import br.com.abril.nds.model.PessoaFisica;
import br.com.abril.nds.model.PessoaJuridica;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class PessoaRepositoryImplTest {
	
	@Autowired
	private SessionFactory sf;
	
	@Autowired
	private PessoaRepositoryImpl pessoaRepository;

	
	@Test
	public void salvarPessoaFisica() {
		PessoaFisica pf = new PessoaFisica();
		pf.setEmail("jose.silva@mail.com");
		pf.setCpf("123.456.789-00");
		pf.setNome("José da Silva");
		pessoaRepository.adicionar(pf);
	}
	
	@Test
	public void salvarPessoaJuridica() {
		PessoaJuridica pj = new PessoaJuridica();
		pj.setCnpj("00.000.000/0001-00");
		pj.setEmail("acme@mail.com");
		pj.setRazaoSocial("ACME CORP");
		pessoaRepository.adicionar(pj);
	}
	
	@Test
	public void buscarPorNome() {
		PessoaFisica pf1 = new PessoaFisica();
		pf1.setEmail("jose.silva@mail.com");
		pf1.setCpf("123.456.789-00");
		pf1.setNome("José da Silva");
		
		PessoaFisica pf2 = new PessoaFisica();
		pf2.setEmail("joao.silva@mail.com");
		pf2.setCpf("321.654.987-00");
		pf2.setNome("João da Silva");
		
		PessoaJuridica pj1 = new PessoaJuridica();
		pj1.setCnpj("00.000.000/0001-00");
		pj1.setEmail("joseltda@mail.com");
		pj1.setRazaoSocial("José Ltda");
		
		PessoaJuridica pj2 = new PessoaJuridica();
		pj2.setCnpj("11.111.111/0001-11");
		pj2.setEmail("acme@mail.com");
		pj2.setRazaoSocial("Acme Ltda");
		
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

	protected void flushClear() {
		getSession().flush();
		getSession().clear();
	}
	
	private Session getSession() {
		return sf.getCurrentSession();
	}

}
