package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.DistribuidorRepository;

public class GeradorArquivoCobrancaBancoServiceImplTest {
	
	@Mock
	private DistribuidorRepository distribuidorRepository;
	
	@Test
	public void prepararGerarArquivoCobrancaCnab() throws IOException {
		
		GeradorArquivoCobrancaBancoServiceImpl serviceMock = 
			Mockito.spy(new GeradorArquivoCobrancaBancoServiceImpl());
		
		Mockito.doReturn(this.prepararDadosCobrancaMock())
			.when(serviceMock).prepararDadosArquivoCobranca();

		Mockito.doReturn(this.getFilePathParametroSistemaMock())
			.when(serviceMock).getFilePathParametroSistema();
		
		Mockito.doReturn(this.getDistribuidorMock())
			.when(serviceMock).getDistribuidor();
		
		serviceMock.prepararGerarArquivoCobrancaCnab();
	}
	
	private Distribuidor getDistribuidorMock() {
		
		PessoaJuridica juridica = new PessoaJuridica();
		
		juridica.setRazaoSocial("Distribuidor");
		juridica.setCnpj("101010");
		
		Distribuidor distribuidor = new Distribuidor();
		
		distribuidor.setDataOperacao(new Date());
		distribuidor.setJuridica(juridica);
		
		return distribuidor;
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
		
		Banco banco = new Banco();
		
		banco.setNumeroBanco("123");
		banco.setAgencia(10L);
		banco.setConta(10L);
		banco.setDvAgencia("1");
		banco.setDvConta("1");
		
		mapaArquivoCobranca.put(banco, lista);
		
		banco = new Banco();
		
		banco.setNumeroBanco("123");
		banco.setAgencia(10L);
		banco.setConta(10L);
		banco.setDvAgencia("1");
		banco.setDvConta("1");
		
		mapaArquivoCobranca.put(banco, lista);
		
		return mapaArquivoCobranca;
	}
}
