package br.com.abril.xrequers.integration.repository.tests;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;

public class ResumoReparteFecharDiaRepositoryImplTest extends AbstractRepositoryTest  {

	private ResumoReparteFecharDiaRepository resumoReparteFecharDiaRepository;
	
	@Test
	public void test_obter_sumarizacao_reparte(){
		
		Calendar c = Calendar.getInstance();
		
		c.clear();
		
		c.set(2014, Calendar.FEBRUARY, 19);
		Date data =  c.getTime();
		
		c.set(2014, Calendar.FEBRUARY, 18);
		Date dataReparteHistoico = c.getTime();
		
		SumarizacaoReparteDTO sumarizacaoReparte = resumoReparteFecharDiaRepository.obterSumarizacaoReparte(data);
		
		assertNotNull(sumarizacaoReparte);
		
	}
	
}

