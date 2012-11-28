package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.vo.PaginacaoVO;


public class FollowupChamadaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FollowupChamadaoRepositoryImpl followupChamadaoRepositoryImpl;
	
	@Test
	public void testarObterConsignadosParaChamadao() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ConsultaFollowupChamadaoDTO> listaConsignados;
		
		FiltroFollowupChamadaoDTO filtro = new FiltroFollowupChamadaoDTO();
		filtro.setPaginacao(paginacao);
		
		listaConsignados = followupChamadaoRepositoryImpl.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(listaConsignados);
		
	}

	@Test
	public void obterConsignadosParaChamadao() {
		FiltroFollowupChamadaoDTO filtro = new  FiltroFollowupChamadaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		followupChamadaoRepositoryImpl.obterConsignadosParaChamadao(filtro);
	}

}
