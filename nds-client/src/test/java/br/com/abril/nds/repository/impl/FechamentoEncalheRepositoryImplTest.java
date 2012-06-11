package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

public class FechamentoEncalheRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Test
	public void testarBuscarConferenciaEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		
		List<FechamentoFisicoLogicoDTO> resultado = this.fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, "asc", "codigo", 2, 20);
		
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void testarBuscarFechamentoEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		
		List<FechamentoEncalhe> resultado = this.fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro);
		
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
	
}
