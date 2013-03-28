package br.com.abril.nds.print;


import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.swing.JApplet;

public class ImpressaoFinalizacaoEncalhe extends JApplet{

	// ========== - Metodo de iniciação do applet - =============
	@Override
	public void init() {

		try {
			try {
				imprimir();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PrintException e) {
			e.printStackTrace();
		}
	}

	public void imprimir() throws URISyntaxException, FileNotFoundException,
			PrintException, IOException {

		String conteudo = "\u001b\u0040" + "TITULO PARA IMPRESSAO" + "\n\r";
		conteudo += "IMPRIMINDO CONTEUDO EM IMPRESSORA MATRICIAL \n\r";

		InputStream ps = null;

		ps = new ByteArrayInputStream(conteudo.getBytes());

		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		DocPrintJob job = null;

		PrintService pserv = PrinterJob.getPrinterJob().getPrintService();
		job = pserv.createPrintJob();

		ImpressaoFinalizacaoEncalheUtil pjDone = new ImpressaoFinalizacaoEncalheUtil(job);
		Doc doc = new SimpleDoc(ps, flavor, null);

		job.print(doc, null);

		// AGUARDA A CONCLUSAO DO TRABALHO
		pjDone.waitForDone();

		ps.close();
	}
}