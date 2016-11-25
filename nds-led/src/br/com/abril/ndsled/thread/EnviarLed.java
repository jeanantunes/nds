package br.com.abril.ndsled.thread;

import java.util.Collections;
import java.util.Comparator;
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
	private List<Produto> lstProdutosAgrupados;
	private JLabel lblStatusBarMessage;
	private JButton btnEnviar;
	private JTextField txtCodigoDeBarras;

	public EnviarLed(JComboBox cbxPortaSerial, JComboBox cbxListaProdutos,
			List<Lancamento> lstLancamentos,
			JTextField txtCatactereReparteZero, List<Cota> lstCotas,
			JLabel lblStatusBarMessage, JButton btnEnviar,
			JTextField txtCodigoDeBarras, List<Produto> lstProdutosAgrupados) {
		super();
		this.cbxPortaSerial = cbxPortaSerial;
		this.cbxListaProdutos = cbxListaProdutos;
		this.lstLancamentos = lstLancamentos;
		this.txtCatactereReparteZero = txtCatactereReparteZero;
		this.lstCotas = lstCotas;
		this.lblStatusBarMessage = lblStatusBarMessage;
		this.btnEnviar = btnEnviar;
		this.txtCodigoDeBarras = txtCodigoDeBarras;
		this.lstProdutosAgrupados = lstProdutosAgrupados;
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
					produtoSelecionado.getCodigoProduto()) == 0
					&& lanc.getEdicaoProduto().compareTo(
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
		produtoSelecionado.setNomeProduto("* "
				+ produtoSelecionado.getNomeProduto());
		

		// Ordena a lista de produtos agrupados por nome.
		Collections.sort(lstProdutosAgrupados, new Comparator<Produto>() {
			@Override
			public int compare(Produto o1, Produto o2) {
				return o1.getNomeProduto().compareToIgnoreCase(
						o2.getNomeProduto());
			}
		});

		// Remove todos itens para adicionar com a nova ordenacao.
		try {
			cbxListaProdutos.removeAllItems();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		
		try {
			cbxListaProdutos.removeAllItems();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		
		
		// Alimenta o Combobox de Produtos com a lista de produtos agrupado
		// do dia selecionado.
		Iterator<Produto> itListProdutosAgrupados = lstProdutosAgrupados
				.iterator();

		while (itListProdutosAgrupados.hasNext()) {
			Produto prd = itListProdutosAgrupados.next();
			cbxListaProdutos.addItem(prd);
		}

		itListProdutosAgrupados = null;
		
		cbxListaProdutos.setSelectedItem(produtoSelecionado);
		cbxListaProdutos.showPopup();
		cbxListaProdutos.hidePopup();
		txtCodigoDeBarras.requestFocus();

		iLanc = null;

		pCom.FecharCom();

		lstCotas = null;
		lstLancamentos = null;
		btnEnviar.setEnabled(true);
		lblStatusBarMessage.setText("");
	}

}
