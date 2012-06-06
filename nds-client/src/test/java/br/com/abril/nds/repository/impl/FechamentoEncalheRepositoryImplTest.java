package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
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
		
		Date dataEncalhe = Calendar.getInstance().getTime();
		
		List<CotaAusenteEncalheDTO> listaCotasAusentes = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe);
		
		Assert.assertNotNull(listaCotasAusentes);
	}
	
}
