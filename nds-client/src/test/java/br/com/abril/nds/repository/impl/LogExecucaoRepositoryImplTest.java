package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;

public class LogExecucaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private LogExecucaoRepositoryImpl logExecucaoRepositoryImpl;
	
	@Before
	public void setup() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), new HashSet<PoliticaCobranca>());
		save(distribuidor);
		
	}
	
	@Test
	public void testarObterInterfaces() {
		
		List<ConsultaInterfacesDTO> listaInterfaces;
		
		listaInterfaces = logExecucaoRepositoryImpl.obterInterfaces();
		
		Assert.assertNotNull(listaInterfaces);
		
	}
	
	@Test
	public void testarObterMensagensLogInterface() {
		
		List<LogExecucaoMensagem> listaMensagens;
		
		Long codigoLogExecucao = 1L;
		
		listaMensagens = logExecucaoRepositoryImpl.obterMensagensLogInterface(codigoLogExecucao);
		
		Assert.assertNotNull(listaMensagens);
		
	}
	
	@Test
	public void testarObterMensagensErroLogInterface() {
		
		List<LogExecucaoMensagem> listaMensagens;
		
		Long codigoLogExecucao = 1L;
		
		listaMensagens = logExecucaoRepositoryImpl.obterMensagensErroLogInterface(codigoLogExecucao);
		
		Assert.assertNotNull(listaMensagens);
		
	}

}
