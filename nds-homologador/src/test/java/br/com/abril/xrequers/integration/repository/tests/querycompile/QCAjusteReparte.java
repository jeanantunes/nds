package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.AjusteReparteRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAjusteReparte extends AbstractRepositoryTest{
	
	@Autowired
	private AjusteReparteRepository ajusteReparteRepository;
	
	@Test
	public void testQCBuscarTodasCotas() {
		
		this.ajusteReparteRepository.buscarTodasCotas(null);
	}
	
	@Test
	public void testQCBuscarPorIdAjuste() {
		this.ajusteReparteRepository.buscarPorIdAjuste(1L);
	}
	
	@Test
	public void testQCBuscarPorIdCota() {
		this.ajusteReparteRepository.buscarPorIdCota(1L);
	}
	
	@Test
	public void testQCExecucaoQuartz() {
		this.ajusteReparteRepository.execucaoQuartz();
	}
	
	@Test
	public void testQCQtdAjusteSegmento() {
		this.ajusteReparteRepository.qtdAjusteSegmento(1L);
	}
	
}

