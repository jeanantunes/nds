package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.TemplateManager.TemplateNames;

public class EmailServiceImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private EmailService emailService;
	
	@Before
	public void setup() {
		
		save(Fixture.criarParametrosEmail());
	}
	
	@Test
	@DirtiesContext
	public void enviarEmail(){
		
		try {
		
			emailService.enviar("Assunto de Teste", "Este e-mail é para teste", new String[]{"sys.discover@gmail.com"});
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
		
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
	
	@Test
	@DirtiesContext
	public void enviarEmailComAnexo() throws FileNotFoundException{
		
		try {
			
			File file = new File ("src/test/resources/testeAnexoEnviodeEmail.pdf");
			
			emailService.enviar("Assunto de Teste com Anexo", "Este e-mail é para teste de anexo", 
						new String[]{"sys.discover@gmail.com"},
						new File[]{file});
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
			
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
	
	
	@Test
	@DirtiesContext
	public void enviarEmailTemplate(){
		
		HashMap<String,Object> param  =  new HashMap<String, Object>();
		param.put("paramTeste", "Template de Teste");
	
		try {
			
			emailService.enviar("Assunto de Teste Template",new String[]{"sys.discover@gmail.com"},TemplateNames.TESTE,param);
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
		
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
	
	@Test
	@DirtiesContext
	public void enviarEmailTemplateComAnexo(){
		
		HashMap<String,Object> param  =  new HashMap<String, Object>();
		param.put("paramTeste", "Template de Teste");
		
		File file = new File ("src/test/resources/testeAnexoEnviodeEmail.pdf");
		
		try {
			
			emailService.enviar("Assunto",new String[]{"sys.discover@gmail.com"},new File[]{file},TemplateNames.TESTE,param);
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
		
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
}
