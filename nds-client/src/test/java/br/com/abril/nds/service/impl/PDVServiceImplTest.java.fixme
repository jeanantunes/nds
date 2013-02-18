package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.service.PdvService;

public class PDVServiceImplTest {

	private PdvService pdvService; 
	
	private List<PeriodoFuncionamentoDTO> periodos;
	private PeriodoFuncionamentoDTO periodo;
	
	@Before
	public void setUp() {
		
		pdvService = new PdvServiceImpl();
		
		periodo = new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA, "00:00", "00:00");
		
		periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		periodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA, "00:00", "00:00"));
		periodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.TERCA_FEIRA, "00:00", "00:00"));
		periodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.QUARTA_FEIRA, "00:00", "00:00"));
		periodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.QUINTA_FEIRA, "00:00", "00:00"));
	}
	
	@Test
	public void getPeriodosPossiveisTest(){
		
		List<TipoPeriodoFuncionamentoPDV> lista = pdvService.getPeriodosPossiveis(periodos);
		
		Assert.assertEquals(lista.size(), 5);
		
	}
	
	@Test
	public void validarPeriodosFalha() throws Exception{

		try {
						
			periodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA, "00:00", "00:00"));
			pdvService.validarPeriodos(periodos);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}
	

	@Test
	public void validarPeriodosSucesso() throws Exception{

		try {
			pdvService.validarPeriodos(periodos);
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
