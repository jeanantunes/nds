package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.TipoProdutoRepository;

public class TipoProdutoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;

	private TipoProduto tipoProduto;
	
	@Before
	public void setUp() {
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		for (int i = 0; i < 3; i++) {
			
			GrupoProduto grupoProduto = GrupoProduto.OUTROS;
			
			if (i > 1) {
				grupoProduto = GrupoProduto.LIVRO;
			} 
			
			NCM ncmRevistas = Fixture.ncm(i+99000642l,"REVISTAS","KG");
			save(ncmRevistas);
			
			this.tipoProduto =
					Fixture.tipoProduto("TipoProduto0"+i, grupoProduto, ncmRevistas, "nbm", 14L + i);
			
			save(tipoProduto);
			
			if (i % 2 == 0) {
				
				for (int j = 0; j < 2; j++) {
					
					Produto produto =
							Fixture.produto(i+j+"1", "Descricao0"+i+j, "Produto0"+i+j, PeriodicidadeProduto.SEMANAL, this.tipoProduto, j, j, new Long(j), TributacaoFiscal. TRIBUTADO);
					
					produto.setEditor(abril);
					save(produto);
				}
			}
		}
	}
	
	@Test
	public void hasProdutoVinculado() {
		
		List<TipoProduto> list = this.tipoProdutoRepository.buscarTodos();
		
		boolean hasProduto;
		
		hasProduto = this.tipoProdutoRepository.hasProdutoVinculado(list.get(0));
		
		Assert.assertTrue(hasProduto);
		
		hasProduto = this.tipoProdutoRepository.hasProdutoVinculado(list.get(1));
		
		Assert.assertFalse(hasProduto);
	}
		
}
