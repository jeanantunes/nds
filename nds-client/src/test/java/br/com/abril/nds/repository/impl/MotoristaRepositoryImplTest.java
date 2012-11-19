package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Motorista;

public class MotoristaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private MotoristaRepositoryImpl motoristaRepositoryImpl;

	@Test
	public void testarBuscarMotoristasPorTransportador() {

		List<Motorista> listaMotoristas;

		Long idTransportador = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		String sortname = "nome";
		String sortorder = "desc";

		listaMotoristas = motoristaRepositoryImpl
				.buscarMotoristasPorTransportador(idTransportador, idsIgnorar,
						sortname, sortorder);

		Assert.assertNotNull(listaMotoristas);
	}
	
//	sortname = "cnh" e sortorder = "desc"
	@Test
	public void testarBuscarMotoristasPorTransportadorSORT() {

		List<Motorista> listaMotoristas;

		Long idTransportador = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		String sortname = "cnh";
		String sortorder = "asc";

		listaMotoristas = motoristaRepositoryImpl
				.buscarMotoristasPorTransportador(idTransportador, idsIgnorar,
						sortname, sortorder);

		Assert.assertNotNull(listaMotoristas);
	}

	@Test
	public void testarRemoverPorId() {

		Long idMotorista = 1L;
		motoristaRepositoryImpl.removerPorId(idMotorista);

		// Assert.assertNull();

	}

	@Test
	public void testarRemoverMotoristas() {

		Long idTransportador = 1L;
		Set<Long> listaMotoristasRemover = new HashSet<Long>();
		listaMotoristasRemover.add(1L);
		listaMotoristasRemover.add(2L);
		listaMotoristasRemover.add(3L);

		motoristaRepositoryImpl.removerMotoristas(idTransportador,
				listaMotoristasRemover);

		// Assert.assertNull();

	}

}
