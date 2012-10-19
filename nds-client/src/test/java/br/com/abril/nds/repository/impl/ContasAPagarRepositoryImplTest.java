package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
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
	}
}
