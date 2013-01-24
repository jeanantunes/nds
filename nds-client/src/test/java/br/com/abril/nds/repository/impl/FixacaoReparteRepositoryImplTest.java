package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.FixacaoReparteRepository;

public class FixacaoReparteRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
	
	private FixacaoReparte fixacaoReparte;
	private Cota cota;
	private Produto produto;
	private Box box;
	private Pessoa pessoa;
	private TipoProduto tipoProduto;
	private NCM ncm;
	
	
	@Before
	public void setup() {
		box = Fixture.boxReparte300();
		pessoa = Fixture.pessoaFisica("81775199720", "fulano@abril.com", "Seu Fulano");
		ncm= Fixture.ncm(new Long("1234"), "", "kg");
		tipoProduto= Fixture.tipoProduto("1234", GrupoProduto.REVISTA, ncm, "1234", new Long(100L));
		cota = Fixture.cota(new Integer(1234), pessoa, SituacaoCadastro.ATIVO, box);
		produto = Fixture.produto("1234", "Veja Curitiba", "veja curitiba", PeriodicidadeProduto.QUINZENAL, tipoProduto, 15, 15, new Long(15L), TributacaoFiscal.TRIBUTADO);
		
		save(box);
		save(pessoa);
		save(cota);
		save(ncm);
		save(tipoProduto);
		save(produto);
		
	}

	@Test
	public void fixacaoReparteCota(){
		fixacaoReparte = new FixacaoReparte();
		fixacaoReparte.setCotaFixada(cota);
		save(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte.getId());
	}
	
	@Test
	public void fixacaoReparteProduto(){
		fixacaoReparte = new FixacaoReparte();
		fixacaoReparte.setProdutoFixado(produto);
		save(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte.getId());
	}
} 