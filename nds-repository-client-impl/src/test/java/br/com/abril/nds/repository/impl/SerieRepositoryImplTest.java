package br.com.abril.nds.repository.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.SerieRepository;

public class SerieRepositoryImplTest  extends AbstractRepositoryImplTest  {
	
	
	@Autowired
	SerieRepository serieRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNext() {
		
		for (int i = 0; i <1000; i++) {
			serieRepository.next(1);			
		}
	}

}
