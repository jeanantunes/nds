package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class LancamentoParcialRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	private ProdutoEdicao produtoEdicaoVeja1;
	private Fornecedor fornecedorFC;
	private Produto produtoVeja;
	
	@Autowired 
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	
	private LancamentoParcial lancamentoParcial;
	
	@Before
	public void setUp() {
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		save(fornecedorFC);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorFC);
		save(produtoVeja);
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 10,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L,
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
		Assert.assertEquals(lancamento, lancamentoParcial);
	}
	
	@Test
	public void buscarLancamentosParciais() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.CODIGO_PRODUTO.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
	@Test
	public void totalBuscaLancamentosParciais() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
				
		Integer total = lancamentoParcialRepository.totalbuscaLancamentosParciais(filtro);
		
		Assert.assertTrue(total == 1);
		
	}
}
