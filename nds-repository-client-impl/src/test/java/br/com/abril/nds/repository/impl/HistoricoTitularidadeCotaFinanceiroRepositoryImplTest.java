package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;

public class HistoricoTitularidadeCotaFinanceiroRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private HistoricoTitularidadeCotaFinanceiroRepositoryImpl historicoTitularidadeCotaFinanceiroRepositoryImpl;
	
	@Test
	public void testarPesquisarTodos() {
		
		List<HistoricoTitularidadeCotaFinanceiro> listaPesquisarTodos;
		
		listaPesquisarTodos = historicoTitularidadeCotaFinanceiroRepositoryImpl.pesquisarTodos();
		
		Assert.assertNotNull(listaPesquisarTodos);
		
	}

}
