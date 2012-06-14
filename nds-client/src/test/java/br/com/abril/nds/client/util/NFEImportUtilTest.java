package br.com.abril.nds.client.util;

import java.io.File;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.exception.ProcessamentoNFEException;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.util.DateUtil;

public class NFEImportUtilTest {

	/**
	 * Constante com caminho da pasta resources
	 */
	private final static String PATH_TEST_RESOURCES = "src/test/resources/";

	/**
	 * Constante com arquivo XML da NFe Assinada
	 */
	private final String XML_ASSINADA = PATH_TEST_RESOURCES + "xmlnfe/assinadaNFe.xml";

	/**
	 * Constante com arquivo XML da ProcNFe
	 */
	private final String XML_PROC_NFE = PATH_TEST_RESOURCES + "xmlnfe/procNFe.xml";

	/**
	 * Constante com arquivo XML da ProcCancNFe
	 */
	private final String XML_PROC_CANC_NFE = PATH_TEST_RESOURCES + "xmlnfe/procCancNfe.xml";
	
	/**
	 * Testa se o preenchimento do RetornoNFEDTO através do arquivo de XML da
	 * NFe assinada está compativel.
	 * 
	 * @throws ProcessamentoNFEException
	 * @throws JAXBException
	 * @throws SAXException
	 */
	@Test
	public void testImpotacaoNFeAssinada() throws ProcessamentoNFEException, JAXBException, SAXException{
		File arquivo = new File(this.XML_ASSINADA);
		
		RetornoNFEDTO retornoNFE = NFEImportUtil.processarArquivoRetorno(arquivo);
		
		RetornoNFEDTO retornoNFEEsperado = new RetornoNFEDTO();
		
		retornoNFEEsperado.setIdNotaFiscal(13162185L);
		retornoNFEEsperado.setChaveAcesso("33111102737654003496550550000483081131621856");
		retornoNFEEsperado.setCpfCnpj("02737654003496");
		retornoNFEEsperado.setProtocolo(null);
		retornoNFEEsperado.setMotivo(null);
		retornoNFEEsperado.setStatus(null);
		retornoNFEEsperado.setDataRecebimento(null);
		
		Assert.assertEquals(retornoNFEEsperado.getIdNotaFiscal(), retornoNFE.getIdNotaFiscal());
		Assert.assertEquals(retornoNFEEsperado.getChaveAcesso(), retornoNFE.getChaveAcesso());
		Assert.assertEquals(retornoNFEEsperado.getCpfCnpj(), retornoNFE.getCpfCnpj());
		Assert.assertEquals(retornoNFEEsperado.getProtocolo(), retornoNFE.getProtocolo());
		Assert.assertEquals(retornoNFEEsperado.getMotivo(), retornoNFE.getMotivo());
		Assert.assertEquals(retornoNFEEsperado.getStatus(), retornoNFE.getStatus());
		Assert.assertEquals(retornoNFEEsperado.getDataRecebimento(), retornoNFE.getDataRecebimento());
		
	}
	
	/**
	 * Testa se o preenchimento do RetornoNFEDTO através do arquivo de XML
	 * ProcNFe está compativel.
	 * 
	 * @throws ProcessamentoNFEException
	 * @throws JAXBException
	 * @throws SAXException
	 */
	@Test
	public void testImpotacaoNFeProcNFe() throws ProcessamentoNFEException, JAXBException, SAXException{
		File arquivo = new File(this.XML_PROC_NFE);
		
		RetornoNFEDTO retornoNFE = NFEImportUtil.processarArquivoRetorno(arquivo);
		
		RetornoNFEDTO retornoNFEEsperado = new RetornoNFEDTO();
		
		retornoNFEEsperado.setIdNotaFiscal(13162185L);
		retornoNFEEsperado.setChaveAcesso("33111102737654003496550550000483081131621856");
		retornoNFEEsperado.setCpfCnpj("02737654003496");
		retornoNFEEsperado.setProtocolo(333110132355391L);
		retornoNFEEsperado.setMotivo(null);
		retornoNFEEsperado.setStatus(Status.AUTORIZADO);
		retornoNFEEsperado.setDataRecebimento(DateUtil.parseData("17/11/2011 08:42:36", "dd/MM/yyyy HH:mm:ss"));
		
		Assert.assertEquals(retornoNFEEsperado.getIdNotaFiscal(), retornoNFE.getIdNotaFiscal());
		Assert.assertEquals(retornoNFEEsperado.getChaveAcesso(), retornoNFE.getChaveAcesso());
		Assert.assertEquals(retornoNFEEsperado.getCpfCnpj(), retornoNFE.getCpfCnpj());
		Assert.assertEquals(retornoNFEEsperado.getProtocolo(), retornoNFE.getProtocolo());
		Assert.assertEquals(retornoNFEEsperado.getMotivo(), retornoNFE.getMotivo());
		Assert.assertEquals(retornoNFEEsperado.getStatus(), retornoNFE.getStatus());
		Assert.assertEquals(retornoNFEEsperado.getDataRecebimento(), retornoNFE.getDataRecebimento());
		
	}
	
	/**
	 * Testa se o preenchimento do RetornoNFEDTO através do arquivo de XML
	 * ProcCancNFe está compativel.
	 * 
	 * @throws ProcessamentoNFEException
	 * @throws JAXBException
	 * @throws SAXException
	 */
	@Test
	public void testImpotacaoNFeProcCancNFe() throws ProcessamentoNFEException, JAXBException, SAXException{
		File arquivo = new File(this.XML_PROC_CANC_NFE);
		
		RetornoNFEDTO retornoNFE = NFEImportUtil.processarArquivoRetorno(arquivo);
		
		RetornoNFEDTO retornoNFEEsperado = new RetornoNFEDTO();
		
		retornoNFEEsperado.setIdNotaFiscal(null);
		retornoNFEEsperado.setChaveAcesso("33111102737654003496550550000483081131621856");
		retornoNFEEsperado.setCpfCnpj(null);
		retornoNFEEsperado.setProtocolo(333110000012466L);
		retornoNFEEsperado.setMotivo("justificativa de testes para cancelamento");
		retornoNFEEsperado.setStatus(Status.CANCELAMENTO_HOMOLOGADO);
		retornoNFEEsperado.setDataRecebimento(DateUtil.parseData("05/04/2011 15:18:00", "dd/MM/yyyy HH:mm:ss"));
		
		Assert.assertEquals(retornoNFEEsperado.getIdNotaFiscal(), retornoNFE.getIdNotaFiscal());
		Assert.assertEquals(retornoNFEEsperado.getChaveAcesso(), retornoNFE.getChaveAcesso());
		Assert.assertEquals(retornoNFEEsperado.getCpfCnpj(), retornoNFE.getCpfCnpj());
		Assert.assertEquals(retornoNFEEsperado.getProtocolo(), retornoNFE.getProtocolo());
		Assert.assertEquals(retornoNFEEsperado.getMotivo(), retornoNFE.getMotivo());
		Assert.assertEquals(retornoNFEEsperado.getStatus(), retornoNFE.getStatus());
		Assert.assertEquals(retornoNFEEsperado.getDataRecebimento(), retornoNFE.getDataRecebimento());
		
	}
	
}
