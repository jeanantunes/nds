package br.com.abril.nds.service.impl;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.BoletoService;

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
		
		Boleto boleto = Fixture.boleto("5", new Date(), new Date(), new Date(), "0", 
                					   100.00, "1", "1", StatusCobranca.PAGO, cota);
		save(boleto);
	}
	
	@Test
	@Ignore
	public void teste() {
		
		boletoService.baixarBoletos(null, null);
	}
	
}
