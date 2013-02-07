package br.com.abril.nds.service.impl;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;

public class ConferenciaEncalheServiceImplTest {
	
	private ConferenciaEncalheServiceImpl conferenciaEncalheService;
	
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	
	
	@Before
	public void setUp() {
		
		ControleConferenciaEncalheCota controleConfEncCota = Fixture.controleConferenciaEncalheCota(
				null, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null);
		
		conferenciaEncalheService = new ConferenciaEncalheServiceImpl();
		
		this.controleConferenciaEncalheCotaRepository = mock(ControleConferenciaEncalheCotaRepository.class);
		
		when(this.controleConferenciaEncalheCotaRepository.buscarPorId(anyLong())).thenReturn(controleConfEncCota);
		
	}
	

	@Test
	public void test() {
		
		ConferenciaEncalheService conf = mock(ConferenciaEncalheServiceImpl.class);
		
		when(conf.gerarSlip(anyLong(), anyBoolean())).thenCallRealMethod();
		
		conf.gerarSlip(1L, false);
		
	}
	
}
