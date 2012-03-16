package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.BoletoService;

@Ignore //TODO: Henrique, corrigir a fixture banco para incluir a carteira
public class BoletoServiceImplTest  extends AbstractRepositoryImplTest {
	
	@Autowired
	private BoletoService boletoService;
	
	@Before
	public void setup() {
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com");
		save(pessoaJuridica);
		
		Box box = Fixture.criarBox("300", "Box 300", TipoBox.REPARTE);
		save(box);
		
		Cota cota = Fixture.cota(1000, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
//		Banco bancoHSBC = Fixture.banco(10L, true, Carteira.COBRANCA_NAO_REGISTRADA, "1010",
//			  							123456L, "1", "1", "Instruções.", Moeda.REAL, "HSBC", "399");
//		save(bancoHSBC);
//		
//		Boleto boleto = Fixture.boleto("5", new Date(), new Date(), new Date(), "0", 
//                					   100.00, "1", "1", StatusCobranca.PAGO, cota, bancoHSBC);
//		save(boleto);
	}
	
	@Test
	@Ignore
	public void teste() {
		
		boletoService.baixarBoletos(null, null, null);
	}
	
	@Test
	public void testeImpressao() throws IOException {
		byte[] b = boletoService.gerarImpressaoBoleto("123");
		Assert.assertTrue(b.length > 0);
	}
	
}
