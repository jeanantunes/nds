package br.com.abril.ndsled.thread;

import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.serialcom.PortaCom;

/**
 * Classe Thread para limpar os dados dos leds.
 * 
 * @author Andre W Silva
 * @since 16/09/2016
 */
public class LimparLed extends Thread {

	private JTextField txtCatactereReparteZero;
	private JComboBox cbxPortaSerial;
	private List<Cota> lstCotas;
	private JLabel lblStatusBarMessage;
	private Logger logger = LogManager.getLogger(LimparLed.class);

	public LimparLed(JTextField txtCatactereReparteZero,
			JComboBox cbxPortaSerial, List<Cota> lstCotas,
			JLabel lblStatusBarMessage) {
		this.txtCatactereReparteZero = txtCatactereReparteZero;
		this.cbxPortaSerial = cbxPortaSerial;
		this.lstCotas = lstCotas;
		this.lblStatusBarMessage = lblStatusBarMessage;
	}

	@Override
	public void run() {

		lblStatusBarMessage.setText("Aguarde, processando...");

		String charZerado = "0";
		if (!txtCatactereReparteZero.getText().isEmpty()) {
			charZerado = txtCatactereReparteZero.getText();
		}

		if (lstCotas != null) {
			PortaCom portaCom = new PortaCom(cbxPortaSerial.getSelectedItem()
					.toString(), 9600, 0);
			portaCom.ObterIdDaPorta();
			portaCom.AbrirPorta();
			Iterator<Cota> cotaIterator = lstCotas.iterator();
			while (cotaIterator.hasNext()) {
				String codLed = String.format("%04d", cotaIterator.next()
						.getCodLed());
				portaCom.EnviarComando("p" + codLed + charZerado + charZerado
						+ charZerado + charZerado);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
			portaCom.FecharCom();
		}

		lblStatusBarMessage.setText("");
	}
}
