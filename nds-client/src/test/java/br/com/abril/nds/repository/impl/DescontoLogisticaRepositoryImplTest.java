package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.DescontoLogistica;

//TODO: Implementar os testes referente a está classe e retirar o "Ignore"

public class DescontoLogisticaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private DescontoLogisticaRepositoryImpl descontoLogisticaRepositoryImpl;
	
	@Test
	public void testarObterTipoDesconto() {
		
		DescontoLogistica descontoLogistica;
		
		Integer tipoDesconto = 1;
		
		descontoLogistica = descontoLogisticaRepositoryImpl.obterPorTipoDesconto(tipoDesconto);
		
//		Assert.assertNull(descontoLogistica);
		
	}

	
}
