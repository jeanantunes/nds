package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

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
	public void salvarPessoaFisica() {
		PessoaFisica pf = Fixture.pessoaFisica("123.456.789-00",
				"jose.silva@mail.com", "José da Silva");
		pessoaRepository.adicionar(pf);
	}
	
	@Test
	public void salvarPessoaJuridica() {
		PessoaJuridica pj = Fixture.pessoaJuridica("ACME CORP",
				"00.000.000/0001-00", "000000000000", "acme@mail.com", "99.999-9");
		pessoaRepository.adicionar(pj);
	}
	
	@Test
	public void buscarPorNome() {
		PessoaFisica pf1 = Fixture.pessoaFisica("123.456.789-00",
				"jose.silva@mail.com", "José da Silva");
		
		PessoaFisica pf2 = Fixture.pessoaFisica("321.654.987-00",
				"joao.silva@mail.com", "João da Silva");

		
		PessoaJuridica pj1 = Fixture.pessoaJuridica("José Ltda",
				"00.000.000/0001-00", "000000000000", "joseltda@mail.com", "99.999-9");

		
		PessoaJuridica pj2 = Fixture.pessoaJuridica("Acme Ltda",
				"11.111.111/0001-11", "000000000000", "acme@mail.com", "99.999-9");
		save(pf1);
		save(pf2);
		save(pj1);
		save(pj2);
		flushClear();
		
		List<Pessoa> pessoas = pessoaRepository.buscarPorNome("José"); 
		Assert.assertTrue(pessoas.size() == 2);
		Assert.assertTrue(pessoas.contains(pf1));
		Assert.assertTrue(pessoas.contains(pj1));
	}
	
	@Test
	public void buscarPorCnpj(){
		String cnpj = "5445.45.45.4.4";
		
		List<PessoaJuridica> pessoaJuridica = pessoaRepository.buscarPorCnpj(cnpj);
		
		Assert.assertNotNull(pessoaJuridica);
	}
	
	@Test
	public void obterSociosPorFiadorIdFiador(){
		Long idFiador = 1L;
		
		List<PessoaFisica> pessoaFisica = pessoaRepository.obterSociosPorFiador(idFiador, null, null);
		
		Assert.assertNotNull(pessoaFisica);
	}
	
	@Test
	public void obterSociosPorFiadorIdsIgnorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		
		List<PessoaFisica> pessoaFisica = pessoaRepository.obterSociosPorFiador(null, idsIgnorar, null);
		
		Assert.assertNotNull(pessoaFisica);
	}
	
	@Test
	public void obterSociosPorFiadorCpfsIgnorar(){
		Set<String> cpfIgnorar = new HashSet<String>();
		cpfIgnorar.add("1.98984.5");
		cpfIgnorar.add("1.65656.5");
		
		List<PessoaFisica> pessoaFisica = pessoaRepository.obterSociosPorFiador(null, null, cpfIgnorar);
		
		Assert.assertNotNull(pessoaFisica);
	}
	
	@Test
	public void buscarPorCPF(){
		String cpf = "857878.787.8";
		
		PessoaFisica pessoaFisica = pessoaRepository.buscarPorCPF(cpf);
	}

	@Test
	public void buscarPorCNPJ(){
		String cnpj = "857878.787.8";
		
		PessoaJuridica pessoaJuridica = pessoaRepository.buscarPorCNPJ(cnpj);
	}
	
	@Test
	public void buscarIdPessoaPorCPF(){
		String cpf = "857878.787.8";
		
		pessoaRepository.buscarIdPessoaPorCPF(cpf);
	}
	
	@Test
	public void buscarIdPessoaPorCNPJ(){
		String cnpj = "857878.787.8";
		
		pessoaRepository.buscarIdPessoaPorCNPJ(cnpj);
	}
}
