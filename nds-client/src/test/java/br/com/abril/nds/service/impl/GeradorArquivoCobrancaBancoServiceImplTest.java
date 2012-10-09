package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.model.cadastro.Banco;

public class GeradorArquivoCobrancaBancoServiceImplTest {
	
	@Test
	public void prepararGerarArquivoCobrancaCnab() throws IOException {
		
		GeradorArquivoCobrancaBancoServiceImpl serviceMock = 
			Mockito.spy(new GeradorArquivoCobrancaBancoServiceImpl());
		
		Mockito.doReturn(this.prepararDadosCobrancaMock())
			.when(serviceMock).prepararDadosArquivoCobranca();

		Mockito.doReturn(this.getFilePathParametroSistemaMock())
			.when(serviceMock).getFilePathParametroSistema();
		
		serviceMock.prepararGerarArquivoCobrancaCnab();
	}
	
	protected File getFilePathParametroSistemaMock() {
		
		return new File("C:\\nds-client\\arquivo_cobranca\\");
	}
	
	private Map<Banco, List<DetalheSegmentoP>> prepararDadosCobrancaMock() {
		
		DetalheSegmentoP detalheSegmentoP = new DetalheSegmentoP();
		
		List<DetalheSegmentoP> lista = new ArrayList<DetalheSegmentoP>();
		
		lista.add(detalheSegmentoP);
		lista.add(detalheSegmentoP);
		lista.add(detalheSegmentoP);
		lista.add(detalheSegmentoP);
		lista.add(detalheSegmentoP);
		
		Map<Banco, List<DetalheSegmentoP>> mapaArquivoCobranca =
			new HashMap<Banco, List<DetalheSegmentoP>>();
		
		mapaArquivoCobranca.put(new Banco(), lista);
		
		mapaArquivoCobranca.put(new Banco(), lista);
		
		return mapaArquivoCobranca;
	}
}
