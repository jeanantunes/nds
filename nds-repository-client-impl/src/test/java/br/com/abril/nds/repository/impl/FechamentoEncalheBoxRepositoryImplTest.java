package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;

public class FechamentoEncalheBoxRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FechamentoEncalheBoxRepositoryImpl fechamentoEncalheBoxRepositoryImpl;
	
	@Test
	public void testarBuscarFechamentosEncalheBox() {
		
		List<FechamentoEncalheBox> listaFechamentoEncalhe;
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		
		listaFechamentoEncalhe = fechamentoEncalheBoxRepositoryImpl.buscarFechamentoEncalheBox(filtro);
		
		Assert.assertNotNull(listaFechamentoEncalhe);
		
	}

}
