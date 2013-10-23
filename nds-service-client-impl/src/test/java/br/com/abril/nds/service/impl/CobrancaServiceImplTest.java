package br.com.abril.nds.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.model.financeiro.Cobranca;

@RunWith( MockitoJUnitRunner.class )
public class CobrancaServiceImplTest {

	@Test
	public void test_obter_mapa(){
		
		
		CobrancaServiceImpl service = new CobrancaServiceImpl();
		
		PagamentoDividasDTO pagamento = null;
		
		List<Cobranca> cobrancas = null;
		
		service.obterMapaComposicaoBaixasFinanceira(pagamento, cobrancas);
		
		
		
	}
	
}
