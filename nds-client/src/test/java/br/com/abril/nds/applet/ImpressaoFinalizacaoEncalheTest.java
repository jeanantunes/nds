package br.com.abril.nds.applet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintException;
import javax.print.PrintService;

import org.apache.poi.util.IOUtils;

import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;
import br.com.abril.nds.util.ImpressaoConstantes;
import br.com.abril.nds.util.ImpressaoMatricialUtil;
import br.com.abril.nds.util.ImpressoraUtil;

public class ImpressaoFinalizacaoEncalheTest {

	public static void main(String[] args) {
		getBoletoTest();
	}
	
/*	
	image/gif; class="[B"
	image/gif; class="java.io.InputStream"
	image/gif; class="java.net.URL"
	image/jpeg; class="[B"
	image/jpeg; class="java.io.InputStream"
	image/jpeg; class="java.net.URL"
	image/png; class="[B"
	image/png; class="java.io.InputStream"
	image/png; class="java.net.URL"
	application/x-java-jvm-local-objectref; class="java.awt.print.Pageable"
	application/x-java-jvm-local-objectref; class="java.awt.print.Printable"
	application/octet-stream; class="[B"
	application/octet-stream; class="java.net.URL"
	application/octet-stream; class="java.io.InputStream"
	*/
	private static void getBoletoTest()  {
		
		InputStream fis;
		try {

			fis = new FileInputStream(new File("C:\\temp\\arquivos_cobranca_boleto.pdf"));
			byte[] buffer = new byte[fis.available()];
			int buff = 0;
			while((buff = fis.available()) != 0){
				fis.read(buffer, 0, buff);
			}
			
			System.out.println(new String(buffer));
			
			new ImpressoraUtil().imprimirRPCEstrategia(buffer, ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
//		File input = new File("C:\\temp\\arquivos_cobranca_boleto.pdf");
//		FileInputStream fis;
//		try {
//			fis = new FileInputStream(input);
//		
//			FileChannel fc = fis.getChannel();
//			ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//	
//			PDFFile curFile=null;
//			PDFPrintPage pages=null;
//			curFile = new PDFFile(bb); // Create PDF Print Page
//			pages = new PDFPrintPage(curFile);
//			PrinterJob pjob = PrinterJob.getPrinterJob();
//	
//	        pjob.setPrintService(ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao());
//	
////			pjob.setJobName("C:\\arquivos_cobranca_boleto222.pdf");
//			Book book = new Book();
//			PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
//			book.append(pages, pformat, curFile.getNumPages());
//			pjob.setPageable(book);
//	
//			// print
//			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//	
//	
//			// Print it
//			pjob.print(aset);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrinterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		InputStream in;
//		try {
//			in = new FileInputStream(new File("C:\\arquivos_cobranca_boleto.pdf"));
//	        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
//	
//	        // find the printing service
//	        AttributeSet attributeSet = new HashAttributeSet();
//	        attributeSet.add(new PrinterName("FX", null));
//	        attributeSet.add(new Copies(1));
//	
//	        PrintService[] services = PrintServiceLookup.lookupPrintServices(
//	                DocFlavor.INPUT_STREAM.PDF, attributeSet);
//	
//	        //create document
//	        Doc doc = new SimpleDoc(in, flavor, null);
//	
//	        // create the print job
////	        PrintService service = services[0];
//	        DocPrintJob job = dpj;
//	
//	        // monitor print job events
//	        ImpressoraThreadUtil watcher = new ImpressoraThreadUtil(job);
//	
//	        System.out.println("Printing...");
//	        job.print(doc, null);
//	
//	        // wait for the job to be done
//	        watcher.waitForDone();
//	        System.out.println("Job Completed!!");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrintException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//        InputStream fis;
//		try {
//			
//			fis = new FileInputStream(new File("C:\\arquivos_cobranca_boleto.pdf"));
//			byte[] buffer = new byte[fis.available()];
//			int buff = 0;
//			while((buff = fis.available()) != 0){
//				fis.read(buffer, 0, buff);
//			}
//			
//			System.out.println(new String(buffer));
//			InputStream stream = new ByteArrayInputStream(buffer);   
//			
//			
//			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
//			Doc doc = new SimpleDoc(stream, flavor, null);  
//			dpj.print(doc, null);
//		
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrintException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
//			try {
//				Desktop.getDesktop().print(new java.io.File("C:\\temp\\logAssinatura.txt"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
        
	}


	private static void imprimirDuasImpressorasTest() {
		
		PrintService Gneric = ImpressoraUtil.getImpressoraByName("\\\\10.37.28.166\\Generic / Text Only");
		PrintService HDU16400 = ImpressoraUtil.getImpressoraByName("\\\\abdcw2k305\\HDU16400");
		
		
		if(HDU16400 != null){
			ImpressaoMatricialUtil emissorNotaFiscalMatricial = new ImpressaoMatricialUtil(null);
			try {
				
				byte[] boleto = IOUtils.toByteArray(new FileInputStream(new File("C:\\arquivos_cobranca_boleto.pdf")));
				byte[] bytesSlip = getDadosSlipMock().getBytes();
				
				ImpressoraUtil impressoraUtil = new ImpressoraUtil();
				impressoraUtil.imprimir(bytesSlip, Gneric);
				impressoraUtil.imprimir(boleto, HDU16400);
				
			} catch (PrintException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Impressora não localizada");
		}
	}
	
	public static String getDadosSlipMock() {
		ConferenciaEncalheServiceImpl conf = new ConferenciaEncalheServiceImpl();
		conf.setSlipDTO(new ImpressaoFinalizacaoEncalheTest().getSlipDtoMock());
		String saida = conf.gerarSlipTxtMatricial().toString();
		saida = saida.replaceAll(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE, ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);
		return saida;
	}
	
	public void gerarSlipTxtMatricialTest() throws PrintException, IOException{
        new ImpressaoMatricialUtil(null).imprimirImpressoraPadrao(getDadosSlipMock());
	}

	public SlipDTO getSlipDtoMock() {
		SlipDTO slipDTO = new SlipDTO();
		slipDTO.setNumeroCota(2);
		slipDTO.setNomeCota("ANGELO FALSARELLA - ME");
		slipDTO.setDataConferencia(new Date());
		slipDTO.setCodigoBox("800");
		slipDTO.setNumSlip(null);
		
		
		ProdutoEdicaoSlipDTO dto1 = new ProdutoEdicaoSlipDTO();
		dto1.setOrdinalDiaConferenciaEncalhe("0");
		dto1.setNomeProduto("NOVA ESCOLA");
		dto1.setNumeroEdicao(259l);
		dto1.setReparte(new BigInteger("7"));
		dto1.setEncalhe(new BigInteger("6"));
		dto1.setPrecoVenda(new BigDecimal("3.4645458"));
		dto1.setValorTotal(new BigDecimal("20.7988588"));
		dto1.setDataRecolhimento(new Date());
		

		ProdutoEdicaoSlipDTO dto2 = new ProdutoEdicaoSlipDTO();
		dto2.setOrdinalDiaConferenciaEncalhe("0");
		dto2.setNomeProduto("RECREIO");
		dto2.setNumeroEdicao(677l);
		dto2.setReparte(new BigInteger("10"));
		dto2.setEncalhe(new BigInteger("10"));
		dto2.setPrecoVenda(new BigDecimal("7.7045458"));
		dto2.setValorTotal(new BigDecimal("77.00"));
		dto2.setDataRecolhimento(new Date());
		
		List<ProdutoEdicaoSlipDTO> lista = new ArrayList<ProdutoEdicaoSlipDTO>();
		lista.add(dto1);
		lista.add(dto2);
		
		slipDTO.setListaProdutoEdicaoSlipDTO(lista);
		
		return slipDTO;
	}
}
