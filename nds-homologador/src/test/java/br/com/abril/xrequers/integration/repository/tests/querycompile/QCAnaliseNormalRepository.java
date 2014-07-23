package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.repository.AnaliseNormalRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCAnaliseNormalRepository extends AbstractRepositoryTest {
	
	@Autowired
	private AnaliseNormalRepository analiseNormalRepository;
	
	@Test
	public void testQCAtualizaReparte() {
		this.analiseNormalRepository.atualizaReparte(1L, 1L, 2L);
	}
	
	@Test
	public void testQCBuscarPorId() {
		this.analiseNormalRepository.buscarPorId(1L);
	}
	
	@Test
	public void testQCBuscaAnaliseNormalPorEstudo() {
		AnaliseNormalQueryDTO filtroDTO = new AnaliseNormalQueryDTO();
		
		filtroDTO.estudoId(1L);
		
		this.analiseNormalRepository.buscaAnaliseNormalPorEstudo(filtroDTO);
	}
	
	@Test
	public void testQCBuscaProdutoParaGrid() {
		this.analiseNormalRepository.buscaProdutoParaGrid(1L);
	}
	
	@Test
	public void testQCLiberarEstudo() {
		this.analiseNormalRepository.liberarEstudo(1L);
	}
}
