package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.repository.ChequeImageRepository;

public class ChequeImageRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private ChequeImageRepository chequeImageRepository;
	
	@Test
	public void getImageChequeTest(){
		
		Long idChequeValor = 1L;
		
		@SuppressWarnings("unused")
		byte[] imagemCheque = chequeImageRepository.getImageCheque(idChequeValor);
		
	}
	
	@Test
	public void getTest(){
				
		long idChequeValor = 1L;
		
		@SuppressWarnings("unused")
		ChequeImage imageCheque = chequeImageRepository.get(idChequeValor);
		
	}

}
