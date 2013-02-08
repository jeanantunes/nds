package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class VendaProdutoRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	VendaProdutoRepository vendaProdutoRepository;
	
	
	
	@Test
	public void buscarVendaPorProdutoCodigo(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setCodigo("1");
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}
	
	@Test
	public void buscarVendaPorProdutoEdicao(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setEdicao(1L);
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}

	@Test
	public void buscarVendaPorProdutoIdFornecedor(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setIdFornecedor(1L);
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}
	
	@Test
	public void buscarVendaPorProdutoOrdenacaoEdicao(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("edicao");
		
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}
	
	@Test
	public void buscarVendaPorProdutoOrdenacaoChamadaCapa(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("chamadaCapa");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}
	
	@Test
	public void buscarVendaPorProdutoOrdenacaoQntResultadoPagina(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		
		List<VendaProdutoDTO> vendaProdutoDTOs = vendaProdutoRepository.buscarVendaPorProduto(filtro);
		
		Assert.assertNotNull(vendaProdutoDTOs);
	}
	
	@Test
	public void buscarLancamentoPorEdicaoCodigo(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setCodigo("2");
		
		List<LancamentoPorEdicaoDTO> lancamentoPorEdicaoDTOs = vendaProdutoRepository.buscarLancamentoPorEdicao(filtro);
		
		Assert.assertNotNull(lancamentoPorEdicaoDTOs);
	}
	
	@Test
	public void buscarLancamentoPorEdicaoEdicao(){
		FiltroVendaProdutoDTO filtro = new FiltroVendaProdutoDTO();
		filtro.setEdicao(1L);
		
		List<LancamentoPorEdicaoDTO> lancamentoPorEdicaoDTOs = vendaProdutoRepository.buscarLancamentoPorEdicao(filtro);
		
		Assert.assertNotNull(lancamentoPorEdicaoDTOs);
	}
	
}
