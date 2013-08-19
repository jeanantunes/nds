package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.repository.FollowupStatusCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class FollowupStatusCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	FollowupStatusCotaRepository followupStatusCotaRepository;
	
	FiltroFollowupStatusCotaDTO filtro;
	
	@Before
	public void setUp(){
		filtro = new FiltroFollowupStatusCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
	}
	
	
	@Test
	public void obterConsignadosParaChamadao (){
				
		List<ConsultaFollowupStatusCotaDTO> consultaFollowupStatusCotaDTOs =
				followupStatusCotaRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(consultaFollowupStatusCotaDTOs);
	}
	
	@Test
	public void obterConsignadosParaChamadaoResultadoPorpagina (){
		
		filtro.getPaginacao().setQtdResultadosPorPagina(2);
		filtro.getPaginacao().setPaginaAtual(1);	
		
		List<ConsultaFollowupStatusCotaDTO> consultaFollowupStatusCotaDTOs =
				followupStatusCotaRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(consultaFollowupStatusCotaDTOs);
	}
	
	@Test
	public void obterConsignadosParaChamadaoOrdenacao (){
		
		filtro.getPaginacao().setSortColumn("column");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<ConsultaFollowupStatusCotaDTO> consultaFollowupStatusCotaDTOs =
				followupStatusCotaRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(consultaFollowupStatusCotaDTOs);
	}

}
