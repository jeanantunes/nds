package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.vo.PaginacaoVO;

public class FollowupPendenciaNFeRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private FollowupPendenciaNFeRepositoryImpl followupPendenciaNFeRepositoryImpl;
	
	@Test
	public void testarObterConsignadosParaChamadao() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ConsultaFollowupPendenciaNFeDTO> listaConsignados;
		
		FiltroFollowupPendenciaNFeDTO filtro = new FiltroFollowupPendenciaNFeDTO();
		filtro.setPaginacao(paginacao);
		
		listaConsignados = followupPendenciaNFeRepositoryImpl.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(listaConsignados);		
		
	}

}
