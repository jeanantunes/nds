package br.com.abril.xrequers.integration.service.tests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;
import br.com.abril.nds.service.integracao.DistribuidorService;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:/applicationContext-test.xml")
public class ConferenciaEncalheServiceImplTest extends AbstractJUnit4SpringContextTests {
    
    @ReplaceWithMock
    @Autowired
    private DistribuidorService distribuidorService;
    
    @ReplaceWithMock
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private ConferenciaEncalheServiceImpl service;
    
    @Before
    public void setUp(){
        
        final List<DiaSemana> listaDiasSemana = new ArrayList<DiaSemana>();
        
        listaDiasSemana.add(DiaSemana.SEGUNDA_FEIRA);
        listaDiasSemana.add(DiaSemana.QUARTA_FEIRA);
        listaDiasSemana.add(DiaSemana.SEXTA_FEIRA);
        
        when(this.distribuidorService.inicioSemana()).thenReturn(DiaSemana.QUARTA_FEIRA);
        
        when(this.grupoRepository.obterDiasOperacaoDiferenciadaCota(Matchers.anyInt(), new Date())).thenReturn(listaDiasSemana);
        
        
    }
    
    public static Date criarData(final int dia, final int mes, final int ano) {
        final Calendar data = criarCalendar(dia, mes, ano, 0, 0, 0);
        return data.getTime();
    }
    
    private static Calendar criarCalendar(final int dia, final int mes, final int ano, final int hora,
            final int minuto, final int segundo) {
        final Calendar data = Calendar.getInstance();
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
        final Date dataChamadaEncalhe = criarData(16, Calendar.JANUARY, 2014);
        
        final Date dataPrimeiroDiaRecolhimento = this.service.obterDataPrimeiroDiaEncalheOperacaoDiferenciada(1, dataChamadaEncalhe);
        
        final Date dataRef = criarData(17, Calendar.JANUARY, 2014);
        
        Assert.assertTrue("Data primeiro dia recolhimento deve ser igual a Sexta feira 17 de Janeiro de 2014", 
                dataRef.compareTo(dataPrimeiroDiaRecolhimento) == 0 );
        
        
    }
    
}