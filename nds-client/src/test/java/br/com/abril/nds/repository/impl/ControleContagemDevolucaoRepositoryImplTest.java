package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;

public class ControleContagemDevolucaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ControleContagemDevolucaoRepository controleContagemDevolucaoRepository;
	
	@Test
	public void testarObterControleContagemDevolucao() {
		
		ControleContagemDevolucao controleContagemDevolucao;
		
		Calendar d = Calendar.getInstance();
		Date dataOperacao = d.getTime();
		
		Long idProdutoEdicao = 1L;
		
		controleContagemDevolucao = controleContagemDevolucaoRepository.obterControleContagemDevolucao(dataOperacao, idProdutoEdicao);
		
//		Assert.assertNull(controleContagemDevolucao);
		
	}
	

}
