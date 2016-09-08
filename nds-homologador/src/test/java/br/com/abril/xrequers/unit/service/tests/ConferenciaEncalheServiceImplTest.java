package br.com.abril.xrequers.unit.service.tests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;
import br.com.abril.nds.service.integracao.DistribuidorService;

@RunWith( MockitoJUnitRunner.class )
public class ConferenciaEncalheServiceImplTest {
	
	@Mock
    private DistribuidorService distribuidorService;
    
    @Mock
    private GrupoRepository grupoRepository;
    
    @InjectMocks
    private ConferenciaEncalheService conferenciaEncalheService = new ConferenciaEncalheServiceImpl();
    
    @Before
    public void setUp(){
    	
    	List<DiaSemana> listaDiasSemana = new ArrayList<DiaSemana>();
    	
    	listaDiasSemana.add(DiaSemana.SEGUNDA_FEIRA);
    	listaDiasSemana.add(DiaSemana.QUARTA_FEIRA);
    	listaDiasSemana.add(DiaSemana.SEXTA_FEIRA);
    	
    	when(distribuidorService.inicioSemanaRecolhimento()).thenReturn(DiaSemana.QUARTA_FEIRA);
    	
    	when(grupoRepository.obterDiasOperacaoDiferenciadaCota(20, new Date())).thenReturn(listaDiasSemana);
    	
    	
    }
    
  
    
    @Test
    public void test_obter_data_primeiro_dia_encalhe_operacao_diferenciada() {
    	
    	//FIXME: Ajustar o testa para testar operacao diferenciada
    	/*
    	//Quinta Feira
    	Date dataChamadaEncalhe = criarData(16, Calendar.JANUARY, 2014);
    	
        Date dataPrimeiroDiaRecolhimento = conferenciaEncalheService.obterDataPrimeiroDiaEncalheOperacaoDiferenciada(1, dataChamadaEncalhe);
    	
    	Date dataRef = criarData(17, Calendar.JANUARY, 2014);
    	
    	Assert.assertTrue("Data primeiro dia recolhimento deve ser igual a Sexta feira 17 de Janeiro de 2014", 
    			dataRef.compareTo(dataPrimeiroDiaRecolhimento) == 0 );
    	*/
    }
    
    @Test
    public void test_obter_data_encalhe_possiveis() {
    	
    	
    	
    }
    

}