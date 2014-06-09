package br.com.abril.xrequers.integration.service.tests;

import static br.com.abril.xrequers.integration.service.tests.TestUtil.criarData;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.impl.GrupoServiceImpl;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * Classe: 
 * GrupoService
 * 
 * Método:
 * obterDatasRecolhimentoOperacaoDiferenciada(Integer numeroCota, Date dataOperacao);
 * 
 * Funcionamento:
 * O método em questão ira retornar uma lista de datas.
 * Esta lista corresponde as datas de encalhe que se concentram 
 * na data de operação passada por parâmetro (Para cotas com
 * operação diferenciada).
 */
@RunWith( MockitoJUnitRunner.class )
public class GrupoServiceImplTest {
	
	@Mock
	private GrupoRepository grupoRepository;
	
	@Mock
	private DistribuidorService distribuidorService;
	
	@Mock
	private CalendarioService calendarioService;
	
	@InjectMocks
	private GrupoServiceImpl grupoServiceImpl = new GrupoServiceImpl();
	
	private List<GrupoCota> obterListaGrupoCota(DiaSemana ... diaSemanas) {
		
		Set<DiaSemana> diasRecolhimento = new HashSet<>();
		
		for(DiaSemana d : diaSemanas) {
			diasRecolhimento.add(d);
		}
		
		GrupoCota gc = new GrupoCota();
		gc.setDiasRecolhimento(diasRecolhimento);
		
		return Arrays.asList(gc);
		
	}
	
	/**
	 * Cenario:
	 * 
	 * Dia Operacao Diferenciada: SEXTA_FEIRA
	 * Dia Inicio Semana: QUARTA_FEIRA
	 * 	
	 * @param numeroCota
	 * @param dataOperacao
	 */
	private void mockar_dados(DiaSemana ...diasOperacaoDiferenciada ) {
		
		List<GrupoCota> listaGrupoCota = obterListaGrupoCota(diasOperacaoDiferenciada);
		
		when(this.grupoRepository.obterListaGrupoCotaPorNumeroCota(Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(listaGrupoCota);

		when(this.distribuidorService.inicioSemanaRecolhimento()).thenReturn(DiaSemana.QUARTA_FEIRA);
		when(this.calendarioService.isFeriadoMunicipalSemOperacao(Mockito.any(Date.class))).thenReturn(false);
		when(this.calendarioService.isFeriadoSemOperacao(Mockito.any(Date.class))).thenReturn(false);
		
		ArgumentMatcher<Date> isSemana = new TestUtil.IsWeek();
		
		when(this.calendarioService.isDiaUtil(argThat(isSemana))).thenReturn(true);
		
	}
	
	
	
	/**
	 * SEM OPERACAO DIFERENCIADA
	 */
	@Test
	public void test_cota_sem_operacao_diferenciada() {
		
		Integer numeroCota = 143;
		Date dataOperacao = criarData(02, Calendar.APRIL, 2014);

		mockar_dados();
		
		List<Date> datas = grupoServiceImpl.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
		
		Assert.assertTrue(
				datas!=null 
				&& 
				datas.size() == 1 
				&& 
				datas.get(0).compareTo(dataOperacao) == 0 );
		
	}
	
	
	
	
	/**
	 * COM DATA DE OPERAÇÃO ANTERIOR AO DIA OPERACAO DIFERENCIADA
	 */
	@Test
	public void test_data_operacao_antes_op_dif() {
		
		Integer numeroCota = 143;
		Date dataOperacao = criarData(02, Calendar.APRIL, 2014);
		
		mockar_dados(DiaSemana.SEXTA_FEIRA);
		
		List<Date> datas = grupoServiceImpl.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
	
		Assert.assertTrue(datas.isEmpty());
	}
	

	/**
	 * COM DATA DE OPERAÇÃO NO DIA OPERACAO DIFERENCIADA.
	 */
	@Test
	public void test_data_operacao_igual_op_dif() {
		
		Integer numeroCota = 143;
		Date dataOperacao = criarData(04, Calendar.APRIL, 2014);
		
		mockar_dados(DiaSemana.SEXTA_FEIRA);
		
		List<Date> datas = grupoServiceImpl.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
		
		List<Date> diasRecolhimentoEsperados = Arrays.asList(
				criarData(2, Calendar.APRIL, 2014), //
				criarData(3, Calendar.APRIL, 2014),
				criarData(4, Calendar.APRIL, 2014),
				criarData(7, Calendar.APRIL, 2014),
				criarData(8, Calendar.APRIL, 2014)
				);
		
		Assert.assertTrue(datas.size() == diasRecolhimentoEsperados.size() &&
				diasRecolhimentoEsperados.containsAll(datas));
		
		
	}

	/**
	 * TENDO 2 DIAS DE OPERACAO DIFERENCIADA (quarta e sexta), COM DATA DE OPERAÇÃO NO DIA OPERACAO DIFERENCIADA (sexta). 
	 */
	@Test
	public void test_data_operacao_igual_op_dif_sexta_2_dias_op_dif() {
		
		Integer numeroCota = 143;
		
		//sexta feira
		Date dataOperacao = criarData(04, Calendar.APRIL, 2014);
		
		mockar_dados(DiaSemana.QUARTA_FEIRA, DiaSemana.SEXTA_FEIRA);
		
		List<Date> datas = grupoServiceImpl.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
		
		List<Date> diasRecolhimentoEsperados = Arrays.asList(
				criarData(3, Calendar.APRIL, 2014),
				criarData(4, Calendar.APRIL, 2014),
				criarData(7, Calendar.APRIL, 2014),
				criarData(8, Calendar.APRIL, 2014)
				);
		
		Assert.assertTrue(datas.size() == diasRecolhimentoEsperados.size() &&
				diasRecolhimentoEsperados.containsAll(datas));
		
	}

	/**
	 * TENDO 2 DIAS DE OPERACAO DIFERENCIADA (quarta e sexta), COM DATA DE OPERAÇÃO NO DIA OPERACAO DIFERENCIADA (quarta). 
	 */
	@Test
	public void test_data_operacao_igual_op_dif_quarta_2_dias_op_dif() {
		
		Integer numeroCota = 143;
		
		//quarta feira
		Date dataOperacao = criarData(02, Calendar.APRIL, 2014);
		
		mockar_dados(DiaSemana.QUARTA_FEIRA, DiaSemana.SEXTA_FEIRA);
		
		List<Date> datas = grupoServiceImpl.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
		
		List<Date> diasRecolhimentoEsperados = Arrays.asList(
				criarData(2, Calendar.APRIL, 2014)
				);
		
		Assert.assertTrue(datas.size() == diasRecolhimentoEsperados.size() &&
				diasRecolhimentoEsperados.containsAll(datas));
		
	}
	
	
}
