package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;


public class MovimentoEstoqueCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Test
	public void testObterConsultaEncalhe() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		Date dataInicial = new GregorianCalendar(2013, 10, 14).getTime();
		Date dataFinal   = new GregorianCalendar(2013, 10, 19).getTime();
		
		filtro.setDataRecolhimentoInicial(dataInicial);
		filtro.setDataRecolhimentoFinal(dataFinal);
		
		List<ConsultaEncalheDTO> consultaEncalhe = 
				this.movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(consultaEncalhe);
	}

}
