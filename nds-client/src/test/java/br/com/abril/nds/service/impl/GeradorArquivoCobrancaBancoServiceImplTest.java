package br.com.abril.nds.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;

public class GeradorArquivoCobrancaBancoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private GeradorArquivoCobrancaBancoService geradorArquivoCobrancaBancoService;
	
	@Test
	public void gerarArquivoRemessa() {
		
		this.geradorArquivoCobrancaBancoService.gerarArquivoCobranca(null);
	}
		
}
