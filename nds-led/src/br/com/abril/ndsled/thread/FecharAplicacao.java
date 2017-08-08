package br.com.abril.ndsled.thread;

import javax.swing.JLabel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Classe Thread criado com finalidade para fechar a aplicacao verificando se
 * outra thread esta ainda em execucao e aguarda ate que ela termine.
 * 
 * @author Andre W Silva
 * @since 16/09/2016
 */
public class FecharAplicacao extends Thread {

	private Thread thread;
	private JLabel lblStatusBarMessage;
	private boolean checkThread = true;
	private Logger logger = LogManager.getLogger(FecharAplicacao.class);

	public FecharAplicacao(Thread thread, JLabel lblStatusBarMessage) {
		this.thread = thread;
		this.lblStatusBarMessage = lblStatusBarMessage;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(100);
			lblStatusBarMessage.setText("Aguarde, encerrando aplicação...");
			while (checkThread) {
				System.out.println("checkThread: "
						+ thread.getState().toString());
				Thread.sleep(100);
				if (thread.getState() == Thread.State.TERMINATED) {
					checkThread = false;
					System.out.println("checkThread: "
							+ thread.getState().toString());
					System.exit(0);
				}
			}
		} catch (InterruptedException e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		}

	}
}
