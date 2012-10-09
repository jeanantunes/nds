package br.com.abril.nds.service.impl;

import java.io.IOException;

import org.junit.Test;

public class GeradorArquivoCobrancaBancoServiceImplTest {
	
	private GeradorArquivoCobrancaBancoServiceImpl service =
		new GeradorArquivoCobrancaBancoServiceImpl();
	
	@Test
	public void gerarArquivoCobranca() throws IOException {
		
		this.service.gerarArquivoCobranca();
	}
		
}
