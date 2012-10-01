package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

public class ItemNovaEnvioRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ItemNotaEnvioRepository itemNotaEnvioRepository;
	
	@Test
	public void obterItensNotaEnvio() {
		
		List<DetalheItemNotaFiscalDTO> listaItemNovaEnvio =
			this.itemNotaEnvioRepository.obterItensNotaEnvio(new Date(), 123);
		
		Assert.assertNotNull(listaItemNovaEnvio);
		
		Assert.assertTrue(listaItemNovaEnvio.isEmpty());
	}
	
	@Test
	public void obterItemNotaEnvio() {
		
		DetalheItemNotaFiscalDTO itemNovaEnvio =
			this.itemNotaEnvioRepository.obterItemNotaEnvio(new Date(), 123, 1L);
		
		Assert.assertNull(itemNovaEnvio);
	}
	
}
