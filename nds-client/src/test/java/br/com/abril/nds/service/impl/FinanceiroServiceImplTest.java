package br.com.abril.nds.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.FinanceiroVO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.FinanceiroService;

@Ignore
public class FinanceiroServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FinanceiroService financeiroService;
	
	private static final Long ID_COTA = 1l;
	
	@Before
	public void setup() {
		
		PessoaFisica pessoaFisica = Fixture.pessoaFisica("123.456.789-00","sys.discover@gmail.com", "Cota da Silva");
		save(pessoaFisica);
		
		Box box = Fixture.criarBox("300", "Box 300", TipoBox.REPARTE);
		save(box);
		
		Cota cota = Fixture.cota(1000, pessoaFisica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
	}
	
	@Test
	public void obterDadosCotaCobranca(){
		@SuppressWarnings("static-access")
		FinanceiroVO financeiroVO = this.financeiroService.obterDadosCotaCobranca(this.ID_COTA);
		Assert.assertTrue(financeiroVO!=null);
	}
	
	
	@Test
	public void obterFormasCobrancaPorCota(){
		List<FormaCobranca> formasCobranca = this.financeiroService.obterFormasCobrancaPorCota();
		Assert.assertNotNull(formasCobranca);
		Assert.assertTrue((formasCobranca.size()>0));
	}
}
