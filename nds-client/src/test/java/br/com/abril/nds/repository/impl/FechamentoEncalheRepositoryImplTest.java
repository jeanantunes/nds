package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

public class FechamentoEncalheRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
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
