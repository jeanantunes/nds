package br.com.abril.xrequers.unit.service.tests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;
import br.com.abril.nds.service.integracao.DistribuidorService;

@RunWith( MockitoJUnitRunner.class )
public class ConferenciaEncalheServiceImplTest {
	
	@Mock
    private DistribuidorService distribuidorService;
    
    @Mock
    private GrupoRepository grupoRepository;
    
    @InjectMocks
    private ConferenciaEncalheServiceImpl conferenciaEncalheService = new ConferenciaEncalheServiceImpl();
    
    @Before
    public void setUp(){
    	
    	List<DiaSemana> listaDiasSemana = new ArrayList<DiaSemana>();
    	
    	listaDiasSemana.add(DiaSemana.SEGUNDA_FEIRA);
    	listaDiasSemana.add(DiaSemana.QUARTA_FEIRA);
    	listaDiasSemana.add(DiaSemana.SEXTA_FEIRA);
    	
    	when(distribuidorService.inicioSemana()).thenReturn(DiaSemana.QUARTA_FEIRA);
    	
    	when(grupoRepository.obterDiasOperacaoDiferenciadaCota(Matchers.anyInt())).thenReturn(listaDiasSemana);
    	
    	
    }
    
    public static Date criarData(int dia, int mes, int ano) {
		Calendar data = criarCalendar(dia, mes, ano, 0, 0, 0);
		return data.getTime();
	}
    
    private static Calendar criarCalendar(int dia, int mes, int ano, int hora,
			int minuto, int segundo) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes);
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.HOUR_OF_DAY, hora);
		data.set(Calendar.MINUTE, minuto);
		data.set(Calendar.SECOND, segundo);
		data.clear(Calendar.MILLISECOND);
		return data;
	}
    
    @Test
    public void test_obter_data_primeiro_dia_encalhe_operacao_diferenciada() {
    	
    	//Quinta Feira
    	Date dataChamadaEncalhe = criarData(16, Calendar.JANUARY, 2014);
    	
        Date dataPrimeiroDiaRecolhimento = conferenciaEncalheService.obterDataPrimeiroDiaEncalheOperacaoDiferenciada(1,
                dataChamadaEncalhe);
    	
    	Date dataRef = criarData(17, Calendar.JANUARY, 2014);
    	
    	Assert.assertTrue("Data primeiro dia recolhimento deve ser igual a Sexta feira 17 de Janeiro de 2014", 
    			dataRef.compareTo(dataPrimeiroDiaRecolhimento) == 0 );
    	
    }

}