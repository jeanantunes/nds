package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.repository.ExpedicaoRepository;

public class ExpedicaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	private FiltroResumoExpedicaoDTO filtro;
	
	@Before
	public void setUp(){
		
		this.filtro = new FiltroResumoExpedicaoDTO();
		this.filtro.setCodigoBox(1);
		this.filtro.setDataLancamento(new Date());
	}
	
	@Test
	public void obterQuantidadeResumoExpedicaoProdutosDoBox(){
		
		this.expedicaoRepository.obterQuantidadeResumoExpedicaoProdutosDoBox(this.filtro);
	}
	
	@Test
	public void obterResumoExpedicaoProdutosDoBox(){
		
		this.expedicaoRepository.obterResumoExpedicaoProdutosDoBox(this.filtro);
	}
}
