package br.com.abril.nds.service.impl;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;

@RunWith( MockitoJUnitRunner.class )
public class ConferenciaEncalheServiceImplTest {
	
	
	private ConferenciaEncalheServiceImpl conferenciaEncalheServiceImpl;
	
	@Mock
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
		
		this.conferenciaEncalheServiceImpl = new ConferenciaEncalheServiceImpl();
		
		when(this.controleConferenciaEncalheCotaRepository.buscarPorId(anyLong())).thenReturn(controleConfEncCota);
		
	}
	

	@Test
	public void test() {
		
		when(this.conferenciaEncalheServiceImpl.gerarSlip(anyLong(), anyBoolean())).thenCallRealMethod();
		
		this.conferenciaEncalheServiceImpl.gerarSlip(1L, false);
		
	}
	
}
