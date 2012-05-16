package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.repository.LancamentoParcialRepository;

public class LancamentoParcialRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	private ProdutoEdicao produtoEdicaoVeja1;
	
	@Autowired 
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	
	private LancamentoParcial lancamentoParcial;
	
	@Before
	public void setUp() {
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 10,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		
		produtoEdicaoVeja1.setParcial(true);
		save(produtoEdicaoVeja1);
		
		
		Date dtInicial = Fixture.criarData(1, 1, 2010);
		Date dtFinal = Fixture.criarData(1, 1, 2011);
		
		lancamentoParcial = Fixture.criarLancamentoParcial(produtoEdicaoVeja1, dtInicial, dtFinal,StatusLancamentoParcial.PROJETADO);
		save(lancamentoParcial);
	}

	@Test
	public void obterLancamentoPorProdutoEdicao() {
		LancamentoParcial lancamento = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicaoVeja1.getId());
		
		Assert.assertTrue( lancamento!= null );
	}

}
