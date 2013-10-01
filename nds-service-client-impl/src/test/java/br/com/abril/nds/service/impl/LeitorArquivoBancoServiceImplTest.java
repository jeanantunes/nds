package br.com.abril.nds.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;

@RunWith( MockitoJUnitRunner.class )
public class LeitorArquivoBancoServiceImplTest {

	@Test
	public void test_leitura_cnab_240() {
		
		LeitorArquivoBancoServiceImpl service = new LeitorArquivoBancoServiceImpl();
		
		ArquivoPagamentoBancoDTO arquivoPagamento = null;
		
		arquivoPagamento = service.obterPagamentosBanco(null, "cnab");
		
	}
	
}
