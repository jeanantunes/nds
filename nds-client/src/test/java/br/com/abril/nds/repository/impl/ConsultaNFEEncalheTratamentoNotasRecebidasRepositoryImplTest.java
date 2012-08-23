package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.repository.ConsultaNFEEncalheTratamentoNotasRecebidasRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ConsultaNFEEncalheTratamentoNotasRecebidasRepositoryImplTest
		extends AbstractRepositoryImplTest {

	@Autowired
	private ConsultaNFEEncalheTratamentoNotasRecebidasRepository consultaNFEEncalheTratamentoNotasRecebidasRepository;
	
	@Test
	public void buscarItensPorNota(){
		
		FiltroConsultaNFEEncalheTratamento filtro = new FiltroConsultaNFEEncalheTratamento();
		filtro.setCodigoNota(1L);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<ItemNotaFiscalPendenteDTO> lista = 
				this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarItensPorNota(
						filtro);
		
		Assert.assertNotNull(lista);
	}
}
