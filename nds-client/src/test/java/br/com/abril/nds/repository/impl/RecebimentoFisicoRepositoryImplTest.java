package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;

public class RecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RecebimentoFisicoRepositoryImpl recebimentoFisicoRepository;

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void obterListaItemRecebimentoFisico() {
		List<RecebimentoFisicoDTO> listaItemRecebimento = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(1L);
	}
		
}
