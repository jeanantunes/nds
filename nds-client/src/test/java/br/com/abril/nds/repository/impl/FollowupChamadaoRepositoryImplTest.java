package br.com.abril.nds.repository.impl;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.repository.FollowupCadastroRepository;
import br.com.abril.nds.vo.PaginacaoVO;


public class FollowupChamadaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	FollowupCadastroRepository repository;
	
	@Before
	public void setup(){
		
		
		
	}
	
	@Test
	public void obterConsignadosParaChamadao() {
		FiltroFollowupCadastroDTO filtro = new  FiltroFollowupCadastroDTO();
		filtro.setPaginacao(new PaginacaoVO());
		repository.obterConsignadosParaChamadao(filtro);
	}
	
	

	@Test
	public void test() {
		fail("Not yet implemented");
		FiltroFollowupChamadaoDTO filtro = new  FiltroFollowupChamadaoDTO();
		repository.obterConsignadosParaChamadao(null);
	}

}
