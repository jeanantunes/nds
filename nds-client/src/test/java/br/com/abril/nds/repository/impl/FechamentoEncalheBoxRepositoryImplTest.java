package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;

public class FechamentoEncalheBoxRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	FechamentoEncalheBoxRepositoryImpl fechamentoEncalheBoxRepositoryImpl;
	
	@Test
	public void buscarFechamentoEncalheBox(){
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		
		filtro.setDataEncalhe(Fixture.criarData(30, Calendar.OCTOBER, 2012));
		filtro.setFornecedorId(1L);
		filtro.setBoxId(1L);
		filtro.setFisico(new ArrayList<Long>());
		filtro.getFisico().add(1L);
		
		List<FechamentoEncalheBox> fechamentoEncalheBox = 
				fechamentoEncalheBoxRepositoryImpl.buscarFechamentoEncalheBox(filtro);
		
		Assert.assertNotNull(fechamentoEncalheBox);
	}


}
