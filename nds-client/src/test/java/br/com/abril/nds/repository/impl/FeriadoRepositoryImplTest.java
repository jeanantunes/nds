package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.util.DateUtil;

public class FeriadoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FeriadoRepository feriadoRepository;

	public static Date FERIADO_SETE_SETEMBRO = DateUtil
			.parseDataPTBR("07/09/2012");

	public static Date FERIADO_ANUAL_FEDERAL = DateUtil
			.parseDataPTBR("15/09/2012");
	public static Date FERIADO_ANUAL_ESTADUAL = DateUtil
			.parseDataPTBR("15/09/2012");
	public static Date FERIADO_ANUAL_MUNICIPAL = DateUtil
			.parseDataPTBR("15/09/2012");

	public static Date FERIADO_FEDERAL = DateUtil.parseDataPTBR("10/09/2012");
	public static Date FERIADO_ESTADUAL = DateUtil.parseDataPTBR("10/09/2012");
	public static Date FERIADO_MUNICIPAL = DateUtil.parseDataPTBR("10/09/2012");
	
	public static UnidadeFederacao UF_SP;

	@Before
	public void setUp() {
		UF_SP = Fixture.criarUnidadeFederacao("SP");
//		save(UF_SP);
		
		Feriado feriado = Fixture.feriado(FERIADO_SETE_SETEMBRO,
				TipoFeriado.FEDERAL, null, null, "Independência do Brasil",
				false, false, true);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_ANUAL_FEDERAL, TipoFeriado.FEDERAL,
				null, null, "Dia de São Thomé", false, false, true);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_ANUAL_ESTADUAL, TipoFeriado.ESTADUAL,
				UF_SP, null, "Dia de São Thomé", false, false, true);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_ANUAL_MUNICIPAL,
				TipoFeriado.MUNICIPAL, null, null, "Dia de São Thomé", false,
				false, true);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_FEDERAL, TipoFeriado.FEDERAL, null,
				null, "Dia de São Thomé", false, false, false);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_ESTADUAL, TipoFeriado.ESTADUAL, UF_SP,
				null, "Dia de São Thomé", false, false, false);
		save(feriado);

		feriado = Fixture.feriado(FERIADO_MUNICIPAL, TipoFeriado.MUNICIPAL,
				null, null, "Dia de São Thomé", false, false, false);
		save(feriado);

	}

	@Test
	public void teste_obter_lista_calendario_feriado_periodo() {

		Date dataInicial = Fixture.criarData(10, Calendar.JANUARY, 2012);

		Date dataFinal = Fixture.criarData(10, Calendar.JANUARY, 2012);

		List<CalendarioFeriadoDTO> resultado = feriadoRepository
				.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal);

		Assert.assertNotNull(resultado);

	}

	@Test
	public void teste_obter_lista_calendario_feriado_data_especifica() {

		Date dtFeriado = FERIADO_ANUAL_FEDERAL;

		Calendar anoAlterado = Calendar.getInstance();

		anoAlterado.setTime(dtFeriado);

		anoAlterado.set(Calendar.YEAR, 1998);

		List<CalendarioFeriadoDTO> resultado = feriadoRepository
				.obterListaCalendarioFeriadoDataEspecifica(anoAlterado
						.getTime());

		Assert.assertNotNull(resultado);

	}

	@Test
	public void teste_obter_feriados() {

		Date dataFeriado = FERIADO_SETE_SETEMBRO;

		TipoFeriado tipoFeriado = TipoFeriado.FEDERAL;

		List<Feriado> listaFeriado = feriadoRepository.obterFeriados(
				dataFeriado, tipoFeriado, null, null);

		Assert.assertNotNull(listaFeriado);

	}

	@Test
	public void teste_obter_lista_calendario_feriado_mensal() {

		List<CalendarioFeriadoDTO> feriados = feriadoRepository
				.obterListaCalendarioFeriadoMensal(9, 2012);

		Assert.assertNotNull(feriados);

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void salvarFeriadoAnualDuplicado() {
		Feriado duplicado = Fixture.feriado(FERIADO_SETE_SETEMBRO,
				TipoFeriado.FEDERAL, null, null, "Independência do Brasil",
				false, false, true);
		feriadoRepository.adicionar(duplicado);
	}
	
	@Test
	public void salvarFeriadoAnualMunicipalDuplicado() {
		Feriado estadual = Fixture.feriado(FERIADO_SETE_SETEMBRO,
				TipoFeriado.ESTADUAL, UF_SP, null, "Apenas para testes",
				false, false, true);
		feriadoRepository.adicionar(estadual);
		flushClear();
		
		Feriado feriadoDB = feriadoRepository.buscarPorId(estadual.getId());
		Assert.assertNotNull(feriadoDB);
		Assert.assertEquals(estadual, feriadoDB);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void alterarFeriadoAnualDuplicado() {
		Feriado feriado = Fixture.feriado(DateUtil.parseDataPTBR("12/10/2012"),
				TipoFeriado.FEDERAL, null, null, "Nossa Senhora Aparecida",
				false, false, true);
		save(feriado);
		flushClear();
		
		feriado.setData(FERIADO_SETE_SETEMBRO);
		feriado.setDescricao("Independência do Brasil");
		feriadoRepository.alterar(feriado);
	}
	
	@Test
	public void alterarFeriadoAnual() {
		Feriado feriado = Fixture.feriado(DateUtil.parseDataPTBR("12/10/2012"),
				TipoFeriado.FEDERAL, null, null, "Nossa Senhora Aparecida",
				false, false, true);
		save(feriado);
		flushClear();
		
		Date dataFinados = DateUtil.parseDataPTBR("01/11/2012");
		feriado.setData(dataFinados);
		feriado.setDescricao("Finados");
		feriadoRepository.alterar(feriado);
		flushClear();
		
		Feriado feriadoDB = feriadoRepository.buscarPorId(feriado.getId());
		Assert.assertNotNull(feriadoDB);
		Assert.assertEquals(feriado.getData(), feriadoDB.getData());
		Assert.assertEquals(feriado.getDescricao(), feriadoDB.getDescricao());
	}

	@Test
	public void buscarFeriadoAnualTipo() {
		Feriado feriado = feriadoRepository.obterFeriadoAnualTipo(
				DateUtil.parseDataPTBR("07/09/2012"), TipoFeriado.FEDERAL);
		Assert.assertNotNull(feriado);
		
		feriado = feriadoRepository.obterFeriadoAnualTipo(
				DateUtil.parseDataPTBR("07/09/2015"), TipoFeriado.FEDERAL);
		Assert.assertNotNull(feriado);
		
		feriado = feriadoRepository.obterFeriadoAnualTipo(
				DateUtil.parseDataPTBR("07/09/2002"), TipoFeriado.FEDERAL);
		Assert.assertNotNull(feriado);
		
		feriado = feriadoRepository.obterFeriadoAnualTipo(
				DateUtil.parseDataPTBR("07/09/2002"), TipoFeriado.ESTADUAL);
		Assert.assertNull(feriado);
	}
	
	
//	TESTE if obterFeriados()
	
//	uf
	@Test
	public void obterFeriadosUf() {
		
		Date dataFeriado = FERIADO_SETE_SETEMBRO;

		TipoFeriado tipoFeriado = TipoFeriado.FEDERAL;

		List<Feriado> listaFeriado = feriadoRepository.obterFeriados(
				dataFeriado, tipoFeriado, "SP", null);

		Assert.assertNotNull(listaFeriado);
		
	}

//	idLocalidade
	@Test
	public void obterFeriadosIdLocalidade() {
		
		Date dataFeriado = FERIADO_SETE_SETEMBRO;

		TipoFeriado tipoFeriado = TipoFeriado.FEDERAL;

		List<Feriado> listaFeriado = feriadoRepository.obterFeriados(
				dataFeriado, tipoFeriado, null, "1");

		Assert.assertNotNull(listaFeriado);
		
	}
	
	

}
