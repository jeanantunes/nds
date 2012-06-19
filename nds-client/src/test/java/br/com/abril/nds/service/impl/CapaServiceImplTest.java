package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.CapaService;

public class CapaServiceImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private CapaService capaService;

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testHasCapaStringLong() {
		capaService.hasCapa("123", 99);
	}

	@Ignore
	@Test
	public void testSaveCapaStringLongStringByteArray() throws IOException  {
		InputStream inputStream = null;
		try {
			inputStream = 
					Thread.currentThread().getContextClassLoader().getResourceAsStream("4-rodas-capa.jpg");
			capaService.saveCapa("77", 99, "image/jpg", inputStream);
		} finally {
			if(inputStream != null){
				inputStream.close();
			}
			
		}
	}

}
