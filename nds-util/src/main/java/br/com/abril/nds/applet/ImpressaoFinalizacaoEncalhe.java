package br.com.abril.nds.applet;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;

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
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					imprimir();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return accessibleContext;
			}
		});

	}

	public static void main(String[] args) {
		try {
			new ImpressaoFinalizacaoEncalhe().gerarSlipTxtMatricialTest();
		} catch (PrintException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void gerarSlipTxtMatricialTest() throws PrintException, IOException{
		
		StringBuffer sb = new StringBuffer();
		EmissorNotaFiscalMatricial e = new EmissorNotaFiscalMatricial(sb);
		
		e.adicionar("TREELOG S/A LOGISTICA E DISTRIBUICAO");
		e.quebrarLinhaEscape();
		e.adicionar("SLIP DE RECOLHIMENTO DE ENCALHE");
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		e.adicionar("Cota: BALBALBALBAL - 545646");
		e.quebrarLinhaEscape();
		e.adicionar("Data: 04/04/2013");
		e.quebrarLinhaEscape();
		e.adicionar("Hora: 15:57:02");
		e.quebrarLinhaEscape();
		e.adicionar("BOX: 4564");
		e.quebrarLinhaEscape(9);//Espaços fim da impressao
		
		String saida = sb.toString();
        System.out.println(saida);
		
//        return saida;
        imprimir(saida);
		
	}
	
	public void imprimir() throws URISyntaxException, FileNotFoundException,
			PrintException, IOException {

		String parameter = getParameter("resultado");
		imprimir(parameter);
	}

	public void imprimir(String saida) throws PrintException, IOException {
		
		if(saida == null || "".equals(saida)){
			saida = "parameter NULL";
		}

		saida = saida.replaceAll(ConstantesAppletImpressao.CARACTER_INDENT_LINEFEED_SCAPE, ConstantesAppletImpressao.CR + ConstantesAppletImpressao.LINE_FEED);
		
		System.out.println("#########>>>>>>>>>>>>>>>"+saida+"<<<<<<<<<<<<<<<<###############");
		
		InputStream ps = null;
		ps = new ByteArrayInputStream(saida.getBytes());
		
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