package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.util.DateUtil;

public class FechamentoEncalheRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Test
	public void testarBuscarConferenciaEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 9, 06);
		////04/10/2012
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		
	//	List<AnaliticoEncalheDTO> resultado = this.fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro);
		
		//Assert.assertNotNull(resultado);
	}
	
	@Test
	public void testarBuscarFechamentoEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		List<FechamentoEncalhe> resultado = this.fechamentoEncalheRepository.buscarFechamentoEncalhe(dataEncalhe.getTime());
		
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void testarBuscarCotasAusentesSucesso() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 5, 20);
		
		List<CotaAusenteEncalheDTO> listaCotasAusentes = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(
				dataEncalhe.getTime(), "asc", "numeroCota", 0, 20);
		
		Assert.assertNotNull(listaCotasAusentes);
		
		listaCotasAusentes = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(
				dataEncalhe.getTime(), "desc", "numeroCota", 20, 20);

		Assert.assertNotNull(listaCotasAusentes);
	}

	@Test
	public void testarBuscarTotalCotasAusentes() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 5, 20);
		
		int total = 
			this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe.getTime());
		
		Assert.assertNotNull(total);
		
	}
	
	@Test
	public void testarBuscarValorTotalEncalhe() {

		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 1, 28);
		
		List<FechamentoFisicoLogicoDTO> listaFechamentoFisicoLogicoDTO =
			this.fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe.getTime(), 13L);
		
		Assert.assertNotNull(listaFechamentoFisicoLogicoDTO);
	}
	
	@Test
	public void testarBuscaQuantidadeConferencia() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		int count = this.fechamentoEncalheRepository.buscaQuantidadeConferencia(dataEncalhe.getTime(), true);
		
		System.out.println(count);
		
		assert count > 0;
	}
	
	@Test
	public void testarObterChamdasEncalhePostergadas() {

		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		Date data = 
			this.fechamentoEncalheRepository.obterChamdasEncalhePostergadas(1L, dataEncalhe.getTime());
	
		Assert.assertNotNull(data);
	}
	
	
	@Test
	public void testarbuscarAnaliticoEncalhe() {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));

		List<AnaliticoEncalheDTO> list = fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro, "", "", 1, 15);
		
		Assert.assertNotNull(list);

	}

}
