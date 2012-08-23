package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ConsultaConsignadoCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ConsultaConsignadoCotaRepository consignadoCotaRepository;
	
	private FiltroConsultaConsignadoCotaDTO filtroConsultaConsignadoCotaDTO;
	
	@Before
	public void setUp(){
		
		this.filtroConsultaConsignadoCotaDTO = new FiltroConsultaConsignadoCotaDTO();
		this.filtroConsultaConsignadoCotaDTO.setPaginacao(new PaginacaoVO());
	}
	
	@Test
	public void buscarConsignadoCota(){
		
		List<ConsultaConsignadoCotaDTO> lista =
				this.consignadoCotaRepository.buscarConsignadoCota(this.filtroConsultaConsignadoCotaDTO, "");
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarMovimentosCotaPeloFornecedor(){
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> lista = 
				this.consignadoCotaRepository.buscarMovimentosCotaPeloFornecedor(
						this.filtroConsultaConsignadoCotaDTO, "");
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarTotalGeralDaCota(){
		
		BigDecimal resultado =
				this.consignadoCotaRepository.buscarTotalGeralDaCota(this.filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void buscarTotalDetalhado(){
		
		List<TotalConsultaConsignadoCotaDetalhado> lista = 
				this.consignadoCotaRepository.buscarTotalDetalhado(this.filtroConsultaConsignadoCotaDTO);
		
		Assert.assertNotNull(lista);
	}
}
