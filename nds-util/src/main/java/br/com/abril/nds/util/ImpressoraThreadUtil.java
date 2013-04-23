package br.com.abril.nds.util;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

 	/**
 * 
 * Classe para controle de impressões
 */
public class ImpressoraThreadUtil {

	// true iff it is safe to close the print job's input stream
	boolean done = false;

	/**
	 * Método para verificar o trabalho de impressão
	 * 
	 * @param DocPrintJog
	 *            Objeto com o trabalho de impressão.
	 * @return Não se aplica.
	 */
	public ImpressoraThreadUtil(DocPrintJob job) {

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
				synchronized (ImpressoraThreadUtil.this) {
					done = true;
					ImpressoraThreadUtil.this.notify();
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
