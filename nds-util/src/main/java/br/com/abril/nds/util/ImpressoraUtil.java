package br.com.abril.nds.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

		PDFFile curFile=new PDFFile(ByteBuffer.wrap(saida));// Create PDF Print Page
		PDFPrintPage pages=new PDFPrintPage(curFile);
		PrinterJob pjob = PrinterJob.getPrinterJob();

        pjob.setPrintService(impressora);

		Paper PAPER = new Paper();  

		PageFormat pformat= pjob.defaultPage();
		
		double x = PAPER.getImageableX()/2.5;
		double y = -150;
		double w = pformat.getWidth()*2;
		double h = pformat.getHeight();
		
		System.out.println("x "+x);
		System.out.println("y "+y);
		System.out.println("w "+w);
		System.out.println("h "+h);

		PAPER.setImageableArea(x, y, w, h);
		
		pformat.setPaper(PAPER);  
		pformat.setOrientation(PageFormat.PORTRAIT);
		
		Book book = new Book();
		book.append(pages, pformat, curFile.getNumPages());
		pjob.setPageable(book);

		// print
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

		// Print it
		pjob.print(aset);
	}
	
//	public static final  A4 = new PageSize(595, 842);
	
	public static PrintService getImpressoraLocalConfiguradaPadrao(){
		return PrinterJob.getPrinterJob().getPrintService();
	}
	
	public static PrintService getImpressoraLocalNaoMatricialNomePadrao(){
//		PropertiesUtil propertiesUtil = new PropertiesUtil(Constantes.NOME_PROPERTIES_NDS_CLIENT);
		String propertyValue = "NDS_NAO_MATRICIAL";//propertiesUtil.getPropertyValue(ImpressaoConstantes.NOME_PADRAO_IMPRESSORA_NAO_MATRICIAL);
		System.out.println("Nome Impressora: "+propertyValue);
		return getImpressoraByName(propertyValue);
	}
	
	public static PrintService getImpressoraLocalMatricialNomePadrao(){
//		PropertiesUtil propertiesUtil = new PropertiesUtil(Constantes.NOME_PROPERTIES_NDS_CLIENT);
		String propertyValue = "NDS_MATRICIAL";//propertiesUtil.getPropertyValue(ImpressaoConstantes.NOME_PADRAO_IMPRESSORA_MATRICIAL);
		System.out.println("Nome Impressora: "+propertyValue);
		return getImpressoraByName(propertyValue);
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
