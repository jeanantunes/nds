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
import br.com.abril.nds.model.fiscal.NCM;
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
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorFC);
		save(produtoVeja);
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 10, new Long(100),
				BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produtoVeja, 
				null, false);
		
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
		Assert.assertEquals(lancamento.getLancamentoInicial(), lancamentoParcial.getLancamentoInicial());
		Assert.assertEquals(lancamento.getRecolhimentoFinal(), lancamentoParcial.getRecolhimentoFinal());
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
	
//	Testes de condições getSqlFromEWhereLancamentosParciais() com buscarLancamentosParciais()
	
//	CODIGO_PRODUTO
	@Test
	public void getSqlFromEWhereLancamentosParciaisCODIGOPRODUTO() {
				
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
	
//	DATA_LANCAMENTO
	@Test
	public void getSqlFromEWhereLancamentosParciaisDATALANCAMENTO() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.DATA_LANCAMENTO.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
//	DATA_RECOLHIMENTO
	@Test
	public void getSqlFromEWhereLancamentosParciaisDATARECOLHIMENTO() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.DATA_RECOLHIMENTO.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
//	NOME_FORNECEDOR
	@Test
	public void getSqlFromEWhereLancamentosParciaisNOMEFORNECEDOR() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.NOME_FORNECEDOR.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
//	NOME_PRODUTO
	@Test
	public void getSqlFromEWhereLancamentosParciaisNOMEPRODUTO() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.NOME_PRODUTO.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
//	NUM_EDICAO
	@Test
	public void getSqlFromEWhereLancamentosParciaisNUMEDICAO() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.NUM_EDICAO.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
	
//	STATUS_PARCIAL
	@Test
	public void getSqlFromEWhereLancamentosParciaisSTATUSPARCIAL() {
				
		FiltroParciaisDTO filtro = new FiltroParciaisDTO();
		filtro.setCodigoProduto(produtoVeja.getCodigo());
		filtro.setDataInicial("10/10/2000");
		filtro.setDataFinal("10/10/2013");
		filtro.setEdicaoProduto(produtoEdicaoVeja1.getNumeroEdicao());
		filtro.setIdFornecedor(fornecedorFC.getId());
		filtro.setNomeProduto(produtoVeja.getNome());
		filtro.setStatus(StatusLancamentoParcial.PROJETADO.name());
		filtro.setPaginacao(new PaginacaoVO(1, 10,"ASC", FiltroParciaisDTO.ColunaOrdenacao.STATUS_PARCIAL.toString()));
				
		List<ParcialDTO> lancamento = lancamentoParcialRepository.buscarLancamentosParciais(filtro);
		
		Assert.assertEquals(lancamento.size(), 1);
		
	}
}
