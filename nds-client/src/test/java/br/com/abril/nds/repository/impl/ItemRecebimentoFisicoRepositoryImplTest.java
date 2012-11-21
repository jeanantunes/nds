package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;

public class ItemRecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ItemRecebimentoFisicoRepositoryImpl itemRecebimentoFisicoRepositoryImpl;
	
	@Test
	public void testarObterItemPorDataLancamentoIdProdutoEdicao() {
		
		ItemRecebimentoFisico itemRecebimentoFisico;
		
		Calendar d = Calendar.getInstance();
		Date dataLancamento = d.getTime();
		
		Long idProdutoEdicao = 1L;
		
		itemRecebimentoFisico = itemRecebimentoFisicoRepositoryImpl.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);
		
		Assert.assertNull(itemRecebimentoFisico);
		
	}
	
	@Test
	public void testarObterItemPorIdRecebimentoFisico() {
		
		List<ItemRecebimentoFisico> listaItemRecebimento;
		
		Long idRecebimentoFisico = 1L;
		
		listaItemRecebimento = itemRecebimentoFisicoRepositoryImpl.obterItemPorIdRecebimentoFisico(idRecebimentoFisico);
		
		Assert.assertNotNull(listaItemRecebimento);
		
	}

}
