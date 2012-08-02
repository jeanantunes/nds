package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.util.DateUtil;

public class FeriadoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	public static Date FERIADO_SETE_SETEMBRO = DateUtil.parseDataPTBR("07/09/2012");
	
	public static Date FERIADO_ANUAL_FEDERAL = DateUtil.parseDataPTBR("15/09/2012");
	public static Date FERIADO_ANUAL_ESTADUAL = DateUtil.parseDataPTBR("15/09/2012");
	public static Date FERIADO_ANUAL_MUNICIPAL = DateUtil.parseDataPTBR("15/09/2012");

	
	public static Date FERIADO_FEDERAL = DateUtil.parseDataPTBR("10/09/2012");
	public static Date FERIADO_ESTADUAL = DateUtil.parseDataPTBR("10/09/2012");
	public static Date FERIADO_MUNICIPAL = DateUtil.parseDataPTBR("10/09/2012");

	
	
	@Before
	public void setUp() {

		Feriado feriado =   Fixture.feriado(FERIADO_SETE_SETEMBRO, TipoFeriado.FEDERAL, null, null , "Independência do Brasil", false, false, true);
		save(feriado);
		
		
		
		feriado =   Fixture.feriado(FERIADO_ANUAL_FEDERAL, TipoFeriado.FEDERAL, null, null , "Dia de São Thomé", false, false, true);
		save(feriado);
		
		feriado =   Fixture.feriado(FERIADO_ANUAL_ESTADUAL, TipoFeriado.ESTADUAL, null, null , "Dia de São Thomé", false, false, true);
		save(feriado);

		feriado =   Fixture.feriado(FERIADO_ANUAL_MUNICIPAL, TipoFeriado.MUNICIPAL, null, null , "Dia de São Thomé", false, false, true);
		save(feriado);


		feriado =   Fixture.feriado(FERIADO_FEDERAL, TipoFeriado.FEDERAL, null, null , "Dia de São Thomé", false, false, false);
		save(feriado);
		
		feriado =   Fixture.feriado(FERIADO_ESTADUAL, TipoFeriado.ESTADUAL, null, null , "Dia de São Thomé", false, false, false);
		save(feriado);

		feriado =   Fixture.feriado(FERIADO_MUNICIPAL, TipoFeriado.MUNICIPAL, null, null , "Dia de São Thomé", false, false, false);
		save(feriado);

		
	}
	
	

	@Test
	public void teste_obter_lista_calendario_feriado_periodo() {

		Date dataInicial = Fixture.criarData(10, Calendar.JANUARY, 2012);
		
		Date dataFinal = Fixture.criarData(10, Calendar.JANUARY, 2012);
		
	    List<CalendarioFeriadoDTO> resultado = feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);
	    
	    Assert.assertNotNull(resultado);
		
	}

	@Test
	public void teste_obter_lista_calendario_feriado_data_especifica(){
		
		Date dtFeriado = FERIADO_ANUAL_FEDERAL;
		
		List<CalendarioFeriadoDTO> resultado = feriadoRepository.obterListaCalendarioFeriadoDataEspecifica(dtFeriado);
		
		Assert.assertNotNull(resultado);
		
	}
	
	@Test
	public void teste_obter_feriados() {

		Date dataFeriado = FERIADO_SETE_SETEMBRO;

		TipoFeriado tipoFeriado = TipoFeriado.FEDERAL;
		
		List<Feriado> listaFeriado = feriadoRepository.obterFeriados(dataFeriado, tipoFeriado, null, null);
		
		Assert.assertNotNull(listaFeriado);
		
	}

	@Test
	public void teste_obter_lista_calendario_feriado_mensal() {
		
		List<CalendarioFeriadoDTO> feriados = feriadoRepository.obterListaCalendarioFeriadoMensal(9, 2012);
		
		Assert.assertNotNull(feriados);
		
	}
	
	
}
