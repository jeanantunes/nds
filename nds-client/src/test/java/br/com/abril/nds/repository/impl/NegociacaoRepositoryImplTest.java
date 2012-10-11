package br.com.abril.nds.repository.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.repository.NegociacaoDividaRepository;

public class NegociacaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private NegociacaoDividaRepository negociacaoDividaRepository;
	
	@Before
	public void setUp(){
	
	}
	
	@Test
	public void obterNegociacaoFollowup(){
		
		negociacaoDividaRepository.obterNegociacaoFollowup(new FiltroFollowupNegociacaoDTO());
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void obterQuantidadeNegociacaoFollowup(){
		
		negociacaoDividaRepository.obterQuantidadeNegociacaoFollowup(new FiltroFollowupNegociacaoDTO());
		
		Assert.assertTrue(true);
	}
}
