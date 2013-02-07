package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;

public class EstoqueProdutoCotaJuramentadoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EstoqueProdutoCotaJuramentadoRepositoryImpl estoqueProdutoCotaJuramentadoRepositoryImpl;
	
	@Test
	public void testarBuscarEstoquePorProdutoECotaNaData() {
		
		EstoqueProdutoCotaJuramentado estoquePorProduto;
		
		Long idProdutoEdicao = 1L;
		Long idCota = 2L;
		
		Calendar d = Calendar.getInstance();
		Date data = d.getTime();
		
		estoquePorProduto = estoqueProdutoCotaJuramentadoRepositoryImpl.buscarEstoquePorProdutoECotaNaData(idProdutoEdicao, idCota, data);
		
		Assert.assertNull(estoquePorProduto);
	}

}
