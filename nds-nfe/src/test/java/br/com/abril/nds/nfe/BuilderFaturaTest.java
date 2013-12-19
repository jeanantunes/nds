package br.com.abril.nds.nfe;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.nfe.model.NotaFiscalDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class BuilderFaturaTest {
	
	@Autowired
	private NotaFiscalNdsServiceTest notaFiscalFaturaNds;
	
	@Test
	public void pesquisarNotaFiscal() {
		
		NotaFiscalDTO nfe = obterNotaFiscal();
		
		notaFiscalFaturaNds.obterDadoFatura(nfe);
		
		Assert.assertNotNull(null);
		
	}
	
	private NotaFiscalDTO obterNotaFiscal() {
		
		NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO();
		
		notaFiscalDTO.setId(1L);
		notaFiscalDTO.setAmbiente(null);
		notaFiscalDTO.setChaveAcesso("1234");
		notaFiscalDTO.setNumero(1L);
		notaFiscalDTO.setProtocolo(null);
		notaFiscalDTO.setSerie("1");
		notaFiscalDTO.setVersao("1");
		
		return notaFiscalDTO;
		
	}
}
