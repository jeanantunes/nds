package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ContasAPagarRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ContasAPagarRepository contasAPagarRepository;
	
	private FiltroContasAPagarDTO getFiltroPesquisaPorDistribuidor(){
		
		List<Long> idsFornecedores = new ArrayList<Long>();
		idsFornecedores.add(1L);
		idsFornecedores.add(2L);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 1, 1);
		
		FiltroContasAPagarDTO filtro = new FiltroContasAPagarDTO();
		filtro.setCe(1);
		filtro.setDataDe(cal.getTime());
		cal.clear();
		cal.set(2099, 1, 1);
		filtro.setDataAte(cal.getTime());
		filtro.setEdicao(1L);
		filtro.setIdsFornecedores(idsFornecedores);
		
		return filtro;
	}
	
    private FiltroContasAPagarDTO getFiltroPesquisaPorProduto(){
		
		List<Long> produtoEdicaoIDs = new ArrayList<Long>();
		produtoEdicaoIDs.add(1L);
		produtoEdicaoIDs.add(2L);
		produtoEdicaoIDs.add(3L);
		produtoEdicaoIDs.add(4L);
		produtoEdicaoIDs.add(5L);
		produtoEdicaoIDs.add(6L);
		produtoEdicaoIDs.add(7L);
		produtoEdicaoIDs.add(8L);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2012, 10, 1);
		
		FiltroContasAPagarDTO filtro = new FiltroContasAPagarDTO();
		filtro.setCe(1);
		//filtro.setDataDe(cal.getTime());
		cal.clear();
		cal.set(2099, 1, 1);
		//filtro.setDataAte(cal.getTime());
		filtro.setEdicao(1L);
		filtro.setProdutoEdicaoIDs(produtoEdicaoIDs);
		
		return filtro;
	}

	@Test
	public void testPesquisarPorDistribuidorCount(){
		
		this.contasAPagarRepository.pesquisarPorDistribuidorCount(this.getFiltroPesquisaPorDistribuidor());
	}
	
	@Test
	public void testPesquisarPorDistribuidor(){
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 10, PaginacaoVO.Ordenacao.ASC.getOrdenacao(), "suplementacao");
		
		FiltroContasAPagarDTO filtroContasAPagarDTO = this.getFiltroPesquisaPorDistribuidor();
		filtroContasAPagarDTO.setPaginacaoVO(paginacaoVO);
		
		List<ContasApagarConsultaPorDistribuidorDTO> lista = 
				this.contasAPagarRepository.pesquisarPorDistribuidor(filtroContasAPagarDTO);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testBuscarTotalPesquisarPorDistribuidor(){
		
		this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(this.getFiltroPesquisaPorDistribuidor(), false);
		
		this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(this.getFiltroPesquisaPorDistribuidor(), true);
	}
	
	@Test
	public void testPesquisarPorProdutoCount(){
		
		Long count = this.contasAPagarRepository.pesquisarCountPorProduto(this.getFiltroPesquisaPorProduto());
	    
		Assert.assertNotNull(count);
	}
	
	@Test
	public void testPesquisarTotaisPorProduto(){
		
		ContasAPagarGridPrincipalProdutoDTO contasAPagarGridPrincipalProdutoDTO = this.contasAPagarRepository.pesquisarTotaisPorProduto(this.getFiltroPesquisaPorProduto());
	    
		Assert.assertNotNull(contasAPagarGridPrincipalProdutoDTO);
	}
	
	@Test
	public void testPesquisarPorProduto(){
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(1, 10, PaginacaoVO.Ordenacao.ASC.getOrdenacao(), "suplementacao");
		
		FiltroContasAPagarDTO filtroContasAPagarDTO = this.getFiltroPesquisaPorProduto();
		filtroContasAPagarDTO.setPaginacaoVO(paginacaoVO);
		
		List<ContasApagarConsultaPorProdutoDTO> lista = this.contasAPagarRepository.pesquisarPorProduto(filtroContasAPagarDTO);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testPesquisarDetalheConsignado(){
		
		FiltroContasAPagarDTO filtro = this.getFiltroPesquisaPorDistribuidor();
		filtro.setDataDetalhe(new Date());
		filtro.setEdicao(null);
		
		List<ContasAPagarConsignadoDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheConsignado(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testPesquisarDetalheEncalhe(){
		
		FiltroContasAPagarDTO filtro = this.getFiltroPesquisaPorDistribuidor();
		filtro.setDataDetalhe(new Date());
		filtro.setEdicao(null);
		
		List<ContasAPagarEncalheDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheEncalhe(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testPesquisarDetalheFaltasSobras(){
		
		FiltroContasAPagarDTO filtro = this.getFiltroPesquisaPorDistribuidor();
		filtro.setDataDetalhe(new Date());
		filtro.setEdicao(null);
		
		List<ContasAPagarFaltasSobrasDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheFaltasSobras(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testPesquisarParcial(){
		
		FiltroContasAPagarDTO filtro = this.getFiltroPesquisaPorDistribuidor();
		filtro.setDataDetalhe(new Date());
		filtro.setEdicao(null);
		filtro.setProduto("544");
		
		List<ContasAPagarParcialDTO> lista = 
				this.contasAPagarRepository.pesquisarParcial(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void testCountPesquisarParcial(){
		
		FiltroContasAPagarDTO filtro = this.getFiltroPesquisaPorDistribuidor();
		filtro.setDataDetalhe(new Date());
		filtro.setEdicao(null);
		filtro.setProduto("544");
		
		this.contasAPagarRepository.countPesquisarParcial(filtro);
	}
}