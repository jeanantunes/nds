package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAlteracaoCotaRepository extends AbstractRepositoryTest {
	
	@Autowired
	private AlteracaoCotaRepository alteracaoCotaRepository;
	
	private FiltroAlteracaoCotaDTO filtro;
	
	@Before
	public void setup() {
		filtro = new FiltroAlteracaoCotaDTO();
	}
	
	@Test
	public void testQCPesquisarAlteracaoCota() {
		this.alteracaoCotaRepository.pesquisarAlteracaoCota(filtro);
	}
	
	@Test
	public void testQCContarAlteracaoCota() {
		this.alteracaoCotaRepository.contarAlteracaoCota(filtro);
	}
	
}
