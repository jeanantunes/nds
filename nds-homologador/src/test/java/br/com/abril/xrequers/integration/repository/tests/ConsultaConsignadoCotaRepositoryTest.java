package br.com.abril.xrequers.integration.repository.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ConsultaConsignadoCotaRepositoryTest extends AbstractRepositoryTest {
	
	@Autowired
	private ConsultaConsignadoCotaRepository consultaConsignadoCotaRepository;
	
	@Test
	public void test_buscar_consignado_cota() {
		
		FiltroConsultaConsignadoCotaDTO filtro = new FiltroConsultaConsignadoCotaDTO();
		
		filtro.setIdCota(1471L);
		
		filtro.setPaginacao(new PaginacaoVO(1, 15, "desc", "dataRecolhimento"));
		
		List<ConsultaConsignadoCotaDTO> lista = consultaConsignadoCotaRepository.buscarConsignadoCota(filtro, true);
		
		Assert.assertNotNull(lista);
		
	}
	
}
