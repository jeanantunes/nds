package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.TelefoneFiadorRepository;

public class TelefoneFiadorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TelefoneFiadorRepository telefoneFiadorRepository;

	private Fiador fiador1;

	@Before
	public void setup() {

		Telefone telefone = Fixture.telefone("019", "259633", "012");
		save(telefone);

		fiador1 = new Fiador();
		Pessoa joao = Fixture.pessoaFisica("12345678904",
				"sys.discover@gmail.com", "João da Silva");
		save(joao);

		fiador1.setInicioAtividade(Fixture.criarData(01, 10, 2010));
		fiador1.setPessoa(joao);
		save(fiador1);

		TelefoneFiador telefoneFiador = new TelefoneFiador();
		telefoneFiador.setTelefone(telefone);
		telefoneFiador.setTipoTelefone(TipoTelefone.CELULAR);
		telefoneFiador.setFiador(fiador1);
		telefoneFiador.setPrincipal(true);
		save(telefoneFiador);

	}

	@Test
	public void removerTelefonesFiador() {
		Collection<Long> listaTelefones = new ArrayList<>();
		listaTelefones.add(1L);
		listaTelefones.add(2L);

		telefoneFiadorRepository.removerTelefonesFiador(listaTelefones);
	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTelefonePrincipalFiador() {
		Telefone telefone = telefoneFiadorRepository
				.pesquisarTelefonePrincipalFiador(null);

	}

	@SuppressWarnings("unused")
	@Test
	public void pesquisarTelefonePrincipalFiadorPorIdFiador() {
		Telefone telefone = telefoneFiadorRepository
				.pesquisarTelefonePrincipalFiador(fiador1.getId());

	}

	@Test
	public void buscarTelefonesFiador() {
		HashSet<Long> idsIgnorar = null;
		
		List<TelefoneAssociacaoDTO> lista = telefoneFiadorRepository
				.buscarTelefonesFiador(fiador1.getId(), idsIgnorar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarTelefonesFiadorPorIdsIgnorar() {
		HashSet<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);

		List<TelefoneAssociacaoDTO> lista = telefoneFiadorRepository
				.buscarTelefonesFiador(fiador1.getId(), idsIgnorar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarTelefonesPessoaPorFiador() {

		List<Telefone> lista = telefoneFiadorRepository
				.buscarTelefonesPessoaPorFiador(fiador1.getId());

		Assert.assertNotNull(lista);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterTelefonePorTelefoneFiador() {

		TelefoneFiador telefone = telefoneFiadorRepository
				.obterTelefonePorTelefoneFiador(1L, fiador1.getId());

	}

	@Test
	public void excluirTelefonesFiador() {
		telefoneFiadorRepository.excluirTelefonesFiador(fiador1.getId());

	}

	@SuppressWarnings("unused")
	@Test
	public void verificarTelefonePrincipalFiador() {
		HashSet<Long> idsIgnorar = null;
		
		boolean retorno = telefoneFiadorRepository
				.verificarTelefonePrincipalFiador(fiador1.getId(),
						idsIgnorar);

	}
	
	@SuppressWarnings("unused")
	@Test
	public void verificarTelefonePrincipalFiadorPorIdsIgnorar() {
		HashSet<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		
		boolean retorno = telefoneFiadorRepository
				.verificarTelefonePrincipalFiador(fiador1.getId(),
						idsIgnorar);

	}

}
