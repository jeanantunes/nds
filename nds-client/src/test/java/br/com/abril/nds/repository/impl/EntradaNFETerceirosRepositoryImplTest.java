package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class EntradaNFETerceirosRepositoryImplTest
		extends AbstractRepositoryImplTest {

	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepositoryRepository;
	
	@Test
	@Ignore
	public void buscarItensPorNota(){
		
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
//		filtro.setCodigoNota(1L);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<ItemNotaFiscalPendenteDTO> lista = 
				this.entradaNFETerceirosRepositoryRepository.buscarItensPorNota(
						filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarNFNotasRecebidas(){
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		
		List<ConsultaEntradaNFETerceirosDTO> lista = entradaNFETerceirosRepositoryRepository.buscarNFNotasRecebidas(filtro, false);
		
		
	}
	
}
