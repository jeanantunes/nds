package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;

@Ignore
public class RecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RecebimentoFisicoRepositoryImpl recebimentoFisicoRepository;

		
	@Test
	public void obterItemNotaPorCnpjNota() {
		
		List<RecebimentoFisicoDTO> listDTO = recebimentoFisicoRepository.obterItemNotaPorCnpjNota("11.111.111/0001-11", "2344242", "345353543");
		
	
		Assert.assertEquals(1, listDTO.size());
		
	}
		
}
