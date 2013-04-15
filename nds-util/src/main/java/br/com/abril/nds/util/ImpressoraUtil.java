package br.com.abril.nds.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;



public class ImpressoraUtil {
	
	public void imprimir(byte[] saida, PrintService impressora) throws PrintException, IOException {
    	
		InputStream ps = null;
		ps = new ByteArrayInputStream(saida);
		
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		DocPrintJob job = null;
		
		job = impressora.createPrintJob();
		
		ImpressoraThreadUtil pjDone = new ImpressoraThreadUtil(job);
		Doc doc = new SimpleDoc(ps, flavor, null);
		
		job.print(doc, null);
		
		// AGUARDA A CONCLUSAO DO TRABALHO
		pjDone.waitForDone();
		
		ps.close();
    }
	
	public void imprimirRPCEstrategia(byte[] saida, PrintService impressora)throws PrintException, IOException, PrinterException{
//		DocPrintJob dpj = ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao().createPrintJob();
		
//		InputStream fis = new ByteArrayInputStream(saida)
//	
//		FileChannel fc = fis.getChannel();
//		ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

		PDFFile curFile=new PDFFile(ByteBuffer.wrap(saida));// Create PDF Print Page
		PDFPrintPage pages=null;
		pages = new PDFPrintPage(curFile);
		PrinterJob pjob = PrinterJob.getPrinterJob();

        pjob.setPrintService(ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao());

//			pjob.setJobName("C:\\arquivos_cobranca_boleto222.pdf");
		Book book = new Book();
		PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
		book.append(pages, pformat, curFile.getNumPages());
		pjob.setPageable(book);

		// print
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();


		// Print it
		pjob.print(aset);
	}
	
	public static PrintService getImpressoraLocalConfiguradaPadrao(){
		return PrinterJob.getPrinterJob().getPrintService();
	}
	
	public static PrintService getImpressoraLocalNaoMatricialNomePadrao(){
//		PropertiesUtil propertiesUtil = new PropertiesUtil(Constantes.NOME_PROPERTIES_NDS_CLIENT);
//		return getImpressoraByName(propertiesUtil.getPropertyValue(ImpressaoConstantes.NOME_PADRAO_IMPRESSORA_NAO_MATRICIAL));
		
		return getImpressoraByName("HDU16400");
	}
	
	public static PrintService getImpressoraLocalMatricialNomePadrao(){
//		PropertiesUtil propertiesUtil = new PropertiesUtil(Constantes.NOME_PROPERTIES_NDS_CLIENT);
//		return getImpressoraByName(propertiesUtil.getPropertyValue(ImpressaoConstantes.NOME_PADRAO_IMPRESSORA_MATRICIAL));
		
		return getImpressoraByName("IMPRESSORA_DGB_PADRAO_MATRICIAL");
	}
	
	public static PrintService getImpressoraByName(String nomeImpressora){
		
		PrintService[] lookupPrintServices = PrinterJob.lookupPrintServices();
		for(PrintService service : lookupPrintServices){
			if(service!=null && nomeImpressora != null && service.getName().contains(nomeImpressora)){
				return  service;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		PrintService impressoraByName = getImpressoraByName(ImpressaoConstantes.NOME_PADRAO_IMPRESSORA_MATRICIAL);
		System.out.println("######### "+impressoraByName);
	}
}
