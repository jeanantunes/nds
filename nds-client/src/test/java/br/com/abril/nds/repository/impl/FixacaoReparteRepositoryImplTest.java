package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
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
	private Usuario usuario;
	
	
	@Before
	public void setup() {
		box = Fixture.boxReparte300();
		pessoa = Fixture.pessoaFisica("81775199720", "fulano@abril.com", "Seu Fulano");
		ncm= Fixture.ncm(new Long("1234"), "", "kg");
		tipoProduto= Fixture.tipoProduto("1234", GrupoProduto.REVISTA, ncm, "1234", new Long(100L));
		cota = Fixture.cota(new Integer(1234), pessoa, SituacaoCadastro.ATIVO, box);
		produto = Fixture.produto("1234", "Veja Curitiba", "veja curitiba", PeriodicidadeProduto.QUINZENAL, tipoProduto, 15, 15, new Long(15L), TributacaoFiscal.TRIBUTADO);
		usuario  = Fixture.usuarioJoao();
		
		fixacaoReparte = new FixacaoReparte();
		fixacaoReparte.setDataHora(new Date());
		fixacaoReparte.setQtdeEdicoes(new Integer("5"));
		fixacaoReparte.setQtdeExemplares(new Integer("100000"));
		fixacaoReparte.setUsuario(usuario);
		
		save(box);
		save(pessoa);
		save(cota);
		save(ncm);
		save(tipoProduto);
		save(produto);
		
	}

	@Test
	public void fixacaoReparteCota(){
		
		fixacaoReparte.setCotaFixada(cota);
		fixacaoReparte.setProdutoFixado(produto);
		fixacaoReparteRepository.adicionar(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte.getId());
	}
	
	
	@Test
	public void obterFixacaoReparteProduto(){
		fixacaoReparte = new FixacaoReparte();
		fixacaoReparte.setProdutoFixado(produto);
		fixacaoReparteRepository.adicionar(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte.getId());
		Assert.assertNotNull(fixacaoReparteRepository.obterFixacoesRepartePorProduto(produto));
		Assert.assertTrue(fixacaoReparteRepository.obterFixacoesRepartePorProduto(produto).size() >0 );
		
	}
	
	@Test
	public void obterFixacaoReparteCota(){
		fixacaoReparte = new FixacaoReparte();
		fixacaoReparte.setProdutoFixado(produto);
		fixacaoReparteRepository.adicionar(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte);
		Assert.assertNotNull(fixacaoReparte.getId());
		Assert.assertNotNull(fixacaoReparteRepository.obterFixacoesRepartePorProduto(produto));
		Assert.assertTrue(fixacaoReparteRepository.obterFixacoesRepartePorProduto(produto).size() >0 );
		
	}
	
	
} 