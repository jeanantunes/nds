package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;

public class TelefoneFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;

	@Test
	public void buscarTelefonesFornecedorTest() {
		this.telefoneFornecedorRepository.buscarTelefonesFornecedor(1L, null);

		Set<Long> set = new HashSet<Long>();
		set.add(2L);

		this.telefoneFornecedorRepository.buscarTelefonesFornecedor(1l, set);
	}

	@Test
	public void removerTelefonesFornecedorTest() {
		Set<Long> set = new HashSet<Long>();
		set.add(2L);

		this.telefoneFornecedorRepository.removerTelefonesFornecedor(set);
	}

	@Test
	public void buscarTelefonesPessoaPorFornecedor() {
		Long idFornecedor = 1L;

		List<Telefone> telefones = telefoneFornecedorRepository
				.buscarTelefonesPessoaPorFornecedor(idFornecedor);

		Assert.assertNotNull(telefones);

	}

	@Test
	public void obterTelefoneFornecedorIdTelefone() {
		Long idTelefone = 1L;

		TelefoneFornecedor telefoneFornecedor = telefoneFornecedorRepository
				.obterTelefoneFornecedor(idTelefone, null);
	}

	@Test
	public void obterTelefoneFornecedorIdFornecedor() {
		Long idFornecedor = 1L;

		TelefoneFornecedor telefoneFornecedor = telefoneFornecedorRepository
				.obterTelefoneFornecedor(null, idFornecedor);

	}
	
	@Test
	public void obterTelefonePrincipal() {
		Long idFornecedor = 1L;

		TelefoneFornecedor telefoneFornecedor = telefoneFornecedorRepository
				.obterTelefonePrincipal(idFornecedor);

	}

}
