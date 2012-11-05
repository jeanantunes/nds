package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;

public class ControleNumeracaoSlipRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ControleNumeracaoSlipRepositoryImpl controleNumeracaoSpliRepositoryImpl;
	
	@Test
	public void testarObterControleNumeracaoSlip() {
		
		ControleNumeracaoSlip controleNumeracaoSlip;
		
		controleNumeracaoSlip = controleNumeracaoSpliRepositoryImpl.obterControleNumeracaoSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE);
		
//		Assert.assertNull(controleNumeracaoSlip);
		
		
	}

}
