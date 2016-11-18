package br.com.abril.ndsled.thread;

import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.Produto;
import br.com.abril.ndsled.serialcom.PortaCom;

/**
 * Classe Thread para manipulacao dos leds em processo paralelo.
 * 
 * @author Andre W Silva
 * @since 16/09/2016
 */
public class EnviarLed extends Thread {

	private JComboBox cbxPortaSerial;
	private JComboBox cbxListaProdutos;
	private List<Lancamento> lstLancamentos;
	private Logger logger = LogManager.getLogger(EnviarLed.class);
	private JTextField txtCatactereReparteZero;
	private List<Cota> lstCotas;
	private JLabel lblStatusBarMessage;
	private JButton btnEnviar;
	private JTextField txtCodigoDeBarras;

	public EnviarLed(JComboBox cbxPortaSerial, JComboBox cbxListaProdutos,
			List<Lancamento> lstLancamentos,
			JTextField txtCatactereReparteZero, List<Cota> lstCotas,
			JLabel lblStatusBarMessage, JButton btnEnviar, JTextField txtCodigoDeBarras) {
		super();
		this.cbxPortaSerial = cbxPortaSerial;
		this.cbxListaProdutos = cbxListaProdutos;
		this.lstLancamentos = lstLancamentos;
		this.txtCatactereReparteZero = txtCatactereReparteZero;
		this.lstCotas = lstCotas;
		this.lblStatusBarMessage = lblStatusBarMessage;
		this.btnEnviar = btnEnviar;
		this.txtCodigoDeBarras = txtCodigoDeBarras;
	}

	@Override
	public void run() {

		lblStatusBarMessage.setText("Aguarde, enviando dados para os LEDs.");
		btnEnviar.setEnabled(false);

		PortaCom pCom = new PortaCom(cbxPortaSerial.getSelectedItem()
				.toString(), 9600, 0);
		pCom.ObterIdDaPorta();
		pCom.AbrirPorta();

		// limparLeds();
		String charZerado = " ";
		if (!txtCatactereReparteZero.getText().isEmpty()) {
			charZerado = txtCatactereReparteZero.getText();
		}

		if (lstCotas != null) {
			Iterator<Cota> cotaIterator = lstCotas.iterator();
			while (cotaIterator.hasNext()) {
				String codLed = String.format("%04d", cotaIterator.next()
						.getCodLed());
				pCom.EnviarComando("p" + codLed + charZerado + charZerado
						+ charZerado + charZerado);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
			cotaIterator = null;
		}

		// enviarLeds
		Produto produtoSelecionado = (Produto) cbxListaProdutos
				.getSelectedItem();
		Iterator<Lancamento> iLanc = lstLancamentos.iterator();
		while (iLanc.hasNext()) {
			Lancamento lanc = iLanc.next();
			if (lanc.getCodigoProduto().compareTo(
					produtoSelecionado.getCodigoProduto()) == 0 && lanc.getEdicaoProduto().compareTo(
							produtoSelecionado.getEdicaoProduto()) == 0) {
				String codLed = String.format("%04d", lanc.getCodigoLed());
				String qtde = String
						.format("%04d", lanc.getQuantidadeReparte());
				pCom.EnviarComando("p" + codLed + qtde);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
		
		produtoSelecionado.setDistribuido(true);
		cbxListaProdutos.showPopup();
		cbxListaProdutos.hidePopup();
		cbxListaProdutos.setSelectedItem(cbxListaProdutos);
		txtCodigoDeBarras.requestFocus();
		
		iLanc = null;

		pCom.FecharCom();

		lstCotas = null;
		lstLancamentos = null;
		btnEnviar.setEnabled(true);
		lblStatusBarMessage.setText("");
	}

}
