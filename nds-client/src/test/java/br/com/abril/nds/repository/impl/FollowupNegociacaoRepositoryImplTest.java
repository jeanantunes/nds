package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;

public class FollowupNegociacaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FollowupNegociacaoRepositoryImpl followupNegociacaoRepositoryImpl;
	
	@Test
	public void testarObterConsignadosParaChamadao() {
		
		List<ConsultaFollowupNegociacaoDTO> listaConsignados;
		
		FiltroFollowupNegociacaoDTO filtro = new FiltroFollowupNegociacaoDTO();
		
		listaConsignados = followupNegociacaoRepositoryImpl.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(listaConsignados);
		
	}

}
