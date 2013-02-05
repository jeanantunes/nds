package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;

public class ItemNotaEnvioRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ItemNotaEnvioRepositoryImpl itemNotaEnvioRepositoryImpl;
	
	
	@Test
	public void testarObterItensNotaEnvio() {
		
		List<DetalheItemNotaFiscalDTO> listaItensNota;
		
		Calendar d = Calendar.getInstance();
		Date dataEmissao = d.getTime();
		
		Integer numeroCota = 1;
		
		listaItensNota = itemNotaEnvioRepositoryImpl.obterItensNotaEnvio(dataEmissao, numeroCota);
		
		Assert.assertNotNull(listaItensNota);
		
	}
	
	@Test
	public void testarObterItemNotaEnvio() {
		
		DetalheItemNotaFiscalDTO detalheItemNotaFiscalDTO;
		
		Calendar d = Calendar.getInstance();
		Date dataEmissao = d.getTime();
		
		Integer numeroCota = 1;
		Long idProdutoEdicao = 1L;
		
		detalheItemNotaFiscalDTO = itemNotaEnvioRepositoryImpl.obterItemNotaEnvio(dataEmissao, numeroCota, idProdutoEdicao);
		
		Assert.assertNull(detalheItemNotaFiscalDTO);
		
	}

}
