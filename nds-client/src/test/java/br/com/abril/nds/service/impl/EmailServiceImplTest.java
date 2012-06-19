package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.TemplateManager.TemplateNames;

public class EmailServiceImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private EmailService emailService;
	
	@Before
	public void setup() {
		
		update(Fixture.criarParametrosEmail());
	}
	//@Ignore(value = "Teste falha com ResourceNotFoundException")
	@Test
	public void enviarEmail(){
		
		try {
		
			emailService.enviar("Assunto de Teste", "Este e-mail é para teste", new String[]{"sys.discover@gmail.com"});
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
		
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
	@Ignore(value = "Teste falha com ResourceNotFoundException")
	@Test
	public void enviarEmailComAnexo() throws IOException{
		
		try {
			
			FileInputStream file = new FileInputStream(new File ("src/test/resources/testeAnexoEnviodeEmail.pdf"));
			
			byte[] anexo = IOUtils.toByteArray(file);
			
			AnexoEmail anexoEmail = new AnexoEmail("testeAnexoEnviodeEmail.pdf",anexo,TipoAnexo.PDF);
			
			emailService.enviar("Assunto de Teste com Anexo", "Este e-mail é para teste de anexo", 
						new String[]{"sys.discover@gmail.com"},
						anexoEmail);
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
			
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
	
	
	@Ignore(value = "Teste falha com ResourceNotFoundException")
	@Test
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
	
	@Ignore(value = "Teste falha com ResourceNotFoundException")
	@Test
	public void enviarEmailTemplateComAnexo() throws IOException{
		
		HashMap<String,Object> param  =  new HashMap<String, Object>();
		param.put("paramTeste", "Template de Teste");
		
		FileInputStream file = new FileInputStream(new File ("src/test/resources/testeAnexoEnviodeEmail.pdf"));
		
		byte[] anexo = IOUtils.toByteArray(file);
		
		AnexoEmail anexoEmail = new AnexoEmail("testeAnexoEnviodeEmail.pdf",anexo,TipoAnexo.PDF);
	
		
		try {
			
			emailService.enviar("Assunto",new String[]{"sys.discover@gmail.com"},new AnexoEmail[]{anexoEmail},TemplateNames.TESTE,param);
			
			Assert.assertTrue("E-mail enviado com sucesso!", true);
		
		} catch (AutenticacaoEmailException e) {
			Assert.assertTrue(e.getMessage(),false);
		}
	}
}
