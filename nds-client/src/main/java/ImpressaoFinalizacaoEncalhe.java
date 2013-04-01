



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

    /*public void init() {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }
    private void createGUI() {
        String text = "Applet's parameters are -- inputStr: " + "sdfsdfsdf" +
                ",   inputInt: " + 34234 +
                ",   paramOutsideJNLPFile: " + "dsfsdfsdf";
        JLabel lbl = new JLabel(text);
        add(lbl);
    }*/
    
	// ========== - Metodo de iniciação do applet - =============
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
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
				return accessibleContext;
			}
		});

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