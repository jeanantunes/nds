package br.com.abril.nds.applet;




import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

 	/**
 * 
 * Classe para controle de impressões em impressoras matriciais.
 */
public class ImpressaoFinalizacaoEncalheUtil {

	// true iff it is safe to close the print job's input stream
	boolean done = false;

	/**
	 * Método para verificar o trabalho de impressão em impressoras
	 * matriciais.
	 * 
	 * @param DocPrintJog
	 *            Objeto com o trabalho de impressão.
	 * @return Não se aplica.
	 */
	ImpressaoFinalizacaoEncalheUtil(DocPrintJob job) {

		// Add a listener to the print job

		job.addPrintJobListener(new PrintJobAdapter() {

			public void printJobCanceled(PrintJobEvent pje) {
				allDone();
			}

			public void printJobCompleted(PrintJobEvent pje) {
				allDone();
			}

			public void printJobFailed(PrintJobEvent pje) {
				allDone();
			}

			public void printJobNoMoreEvents(PrintJobEvent pje) {
				allDone();
			}

			void allDone() {
				synchronized (ImpressaoFinalizacaoEncalheUtil.this) {
					done = true;
					ImpressaoFinalizacaoEncalheUtil.this.notify();
				}
			}
		});
	}

	/**
	 * @version 1.0 Método para aguardar a finalização do trabalho de
	 *          impressao.
	 * @return Não se aplica.
	 */
	public synchronized void waitForDone() {
		try {
			while (!done) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}
}
