package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.repository.FollowupCadastroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class FollowupCadastroRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	FollowupCadastroRepository followupCadastroRepository;
	
	FiltroFollowupCadastroDTO filtro;
	
	@Before
	public void setUp(){
		filtro = new FiltroFollowupCadastroDTO();
		
		filtro.setPaginacao(new PaginacaoVO());
}
	
	@Test
	public void obterConsignadosParaChamadao(){
		
		List <ConsultaFollowupCadastroDTO> consultaFollowupCadastroDTO = followupCadastroRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(consultaFollowupCadastroDTO);
		
	}
	
	@Test
	public void obterConsignadosParaChamadaoResultadoPorPagina(){

		filtro.getPaginacao().setQtdResultadosPorPagina(2);
		filtro.getPaginacao().setPaginaAtual(1);
		
		List <ConsultaFollowupCadastroDTO> consultaFollowupCadastroDTO = followupCadastroRepository.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(consultaFollowupCadastroDTO);
		
	}

}
