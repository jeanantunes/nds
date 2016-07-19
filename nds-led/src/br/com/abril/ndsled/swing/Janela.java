package br.com.abril.ndsled.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import br.com.abril.ndsled.actions.AppActions;
import br.com.abril.ndsled.exceptions.CarregarLancamentoException;
import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.Produto;
import br.com.abril.ndsled.serialcom.PortaCom;
import br.com.abril.ndsled.serialcom.SerialCom;

public class Janela {

	private JFrame frmProjetoLedV;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mnitSair;
	private JPanel pnlConfiguracao;
	private JTextField txtProdutoSelecionado;
	private JTextField txtQuantidadeCotas;
	private JTextField txtTotalDoProduto;
	private JComboBox cbxPortaSerial;
	private JComboBox cbxListaProdutos;
	private JLabel lblData;
	private JLabel lblListaProdutos;
	private JLabel lblProdutoSelecionado;
	private JLabel lblQuantidadeCotas;
	private JLabel lblTotalDoProduto;
	private JLabel lblPortaSerial;
	private JLabel lblStatusBarMessage;
	private JButton btnEnviar;
	private JButton btnReset;
	private JButton btnAcenderLeds;
	private JButton btnSalvarCotaLed;
	private JScrollPane pnlCotaLed;
	private JTable tblCotaLed;
	private JButton btnApagarLeds;
	private JDatePickerImpl pckDataLancamento;
	private List<Lancamento> lstLancamentos;
	private List<Produto> lstProdutosAgrupados;
	private List<Cota> lstCotas;

	public JFrame getFrmProjetoLedV() {
		return frmProjetoLedV;
	}

	public Janela() {
		initialize();

		SerialCom serial = new SerialCom();
		serial.ListarPortas();

		List<String> portas = serial.ObterPortas();
		// String[] portas = serial.ObterPortas();
		Iterator<String> iPortas = portas.iterator();
		while (iPortas.hasNext()) {
			// System.out.println(iPortas.next());
			cbxPortaSerial.addItem(iPortas.next());

		}
	}

	private void initialize() {
		frmProjetoLedV = new JFrame();
		frmProjetoLedV.setTitle("Projeto LED v0.1");
		frmProjetoLedV.setResizable(false);
		frmProjetoLedV.setBounds(100, 100, 978, 452);
		frmProjetoLedV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		frmProjetoLedV.setJMenuBar(menuBar);

		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		mnitSair = new JMenuItem("Sair");
		mnitSair.setSelectedIcon(null);
		mnitSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnMenu.add(mnitSair);
		frmProjetoLedV.getContentPane().setLayout(null);

		lblData = new JLabel("Data Lan\u00E7amento:");
		lblData.setBounds(10, 10, 277, 14);
		frmProjetoLedV.getContentPane().add(lblData);

		lblListaProdutos = new JLabel("Lista Produtos:");
		lblListaProdutos.setBounds(10, 64, 277, 14);
		frmProjetoLedV.getContentPane().add(lblListaProdutos);

		cbxListaProdutos = new JComboBox();
		cbxListaProdutos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Produto produtoSelecionado = (Produto) cbxListaProdutos.getSelectedItem();
				// System.out.println(produtoSelecionado);
				// System.out.println(produtoSelecionado.getCodigoCota());
				// System.out.println(produtoSelecionado.getQuantidadeReparte());
			}
		});
		cbxListaProdutos.setBounds(10, 83, 277, 20);
		frmProjetoLedV.getContentPane().add(cbxListaProdutos);

		lblProdutoSelecionado = new JLabel("Produto:");
		lblProdutoSelecionado.setBounds(10, 114, 277, 14);
		frmProjetoLedV.getContentPane().add(lblProdutoSelecionado);

		txtProdutoSelecionado = new JTextField();
		txtProdutoSelecionado.setEnabled(false);
		txtProdutoSelecionado.setBounds(10, 139, 277, 20);
		frmProjetoLedV.getContentPane().add(txtProdutoSelecionado);
		txtProdutoSelecionado.setColumns(10);

		lblQuantidadeCotas = new JLabel("Quantidade Cotas:");
		lblQuantidadeCotas.setBounds(10, 170, 277, 14);
		frmProjetoLedV.getContentPane().add(lblQuantidadeCotas);

		txtQuantidadeCotas = new JTextField();
		txtQuantidadeCotas.setEnabled(false);
		txtQuantidadeCotas.setColumns(10);
		txtQuantidadeCotas.setBounds(10, 251, 277, 20);
		frmProjetoLedV.getContentPane().add(txtQuantidadeCotas);

		lblTotalDoProduto = new JLabel("Total do Produto:");
		lblTotalDoProduto.setBounds(10, 226, 277, 14);
		frmProjetoLedV.getContentPane().add(lblTotalDoProduto);

		txtTotalDoProduto = new JTextField();
		txtTotalDoProduto.setEnabled(false);
		txtTotalDoProduto.setColumns(10);
		txtTotalDoProduto.setBounds(10, 195, 277, 20);
		frmProjetoLedV.getContentPane().add(txtTotalDoProduto);

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnviar.setBounds(297, 138, 89, 23);
		frmProjetoLedV.getContentPane().add(btnEnviar);

		btnReset = new JButton("Reset");
		btnReset.setBounds(297, 194, 89, 23);
		frmProjetoLedV.getContentPane().add(btnReset);

		pnlConfiguracao = new JPanel();
		pnlConfiguracao.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Configura\u00E7\u00E3o",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		pnlConfiguracao.setBounds(760, 10, 202, 158);
		frmProjetoLedV.getContentPane().add(pnlConfiguracao);
		pnlConfiguracao.setLayout(null);

		cbxPortaSerial = new JComboBox();
		cbxPortaSerial.setBounds(10, 47, 89, 20);
		pnlConfiguracao.add(cbxPortaSerial);

		btnAcenderLeds = new JButton("Acender Leds");
		btnAcenderLeds
				.setToolTipText("Acende todos com 8888 os Leds por 10 segundos para verificar os luminosos.");
		btnAcenderLeds.setBounds(10, 78, 182, 23);
		pnlConfiguracao.add(btnAcenderLeds);

		lblPortaSerial = new JLabel("Porta Serial");
		lblPortaSerial.setBounds(10, 22, 89, 14);
		pnlConfiguracao.add(lblPortaSerial);
		lblPortaSerial.setHorizontalAlignment(SwingConstants.LEFT);

		btnApagarLeds = new JButton("Apagar Leds");
		btnApagarLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					pCom.EnviarComando("p00020000");
					pCom.EnviarComando("p00490000");
					Thread.sleep(100);
					pCom.FecharCom();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		btnApagarLeds.setToolTipText("");
		btnApagarLeds.setBounds(10, 112, 182, 23);
		pnlConfiguracao.add(btnApagarLeds);

		pnlCotaLed = new JScrollPane();
		pnlCotaLed.setBorder(new TitledBorder(null, "Cota / Porta",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlCotaLed.setBounds(416, 10, 202, 305);
		frmProjetoLedV.getContentPane().add(pnlCotaLed);
		pnlCotaLed.setLayout(null);

		btnSalvarCotaLed = new JButton("Salvar");
		btnSalvarCotaLed.setBounds(99, 268, 89, 23);
		pnlCotaLed.add(btnSalvarCotaLed);

		
		
		tblCotaLed = new JTable(new Object[][] { { "", "" }, { "", "" }}, new String[] { "Cota","Led" });
		tblCotaLed
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		
		//tblCotaLed.setModel(new DefaultTableModel());
		tblCotaLed.getColumnModel().getColumn(0).setResizable(false);
		tblCotaLed.getColumnModel().getColumn(1).setResizable(false);
		tblCotaLed.setBounds(10, 28, 178, 229);
		pnlCotaLed.add(tblCotaLed);
		btnAcenderLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					pCom.EnviarComando("p00020010");
					pCom.EnviarComando("p00490007");
					Thread.sleep(100);
					pCom.FecharCom();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});

		UtilDateModel model = new UtilDateModel();
		// model.setDate(20,04,2014);
		// Need this...
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		// Don't know about the formatter, but there it is...
		pckDataLancamento = new JDatePickerImpl(datePanel,
				new DateLabelFormatter());
		pckDataLancamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date selectedDate = (Date) pckDataLancamento.getModel()
						.getValue();
				txtTotalDoProduto.setText(selectedDate.toString());
				carregarLancamento((Date) pckDataLancamento.getModel()
						.getValue());
			}
		});
		pckDataLancamento.setBounds(10, 30, 124, 23);
		frmProjetoLedV.getContentPane().add(pckDataLancamento);

		JPanel pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		FlowLayout fl_pnlStatusBar = (FlowLayout) pnlStatusBar.getLayout();
		fl_pnlStatusBar.setVgap(1);
		fl_pnlStatusBar.setAlignment(FlowLayout.LEADING);
		pnlStatusBar.setBounds(0, 380, 972, 23);
		frmProjetoLedV.getContentPane().add(pnlStatusBar);

		lblStatusBarMessage = new JLabel("");
		pnlStatusBar.add(lblStatusBarMessage);
	}

	private void carregarLancamento(Date date) {
		try {
			lstLancamentos = AppActions.carregarLancamento(date);
			Iterator<Lancamento> iListLancamentos = lstLancamentos.iterator();
			lstProdutosAgrupados = new ArrayList<Produto>();
			while (iListLancamentos.hasNext()) {
				Lancamento it1 = iListLancamentos.next();
				
				if (lstProdutosAgrupados.size() == 0) {
					Produto pd = new Produto();
					pd.setCodigoProduto(it1.getCodigoProduto());
					pd.setDesconto(it1.getDesconto());
					pd.setEdicaoProduto(it1.getEdicaoProduto());
					pd.setNomeProduto(it1.getNomeProduto());
					pd.setPrecoCapa(it1.getPrecoCapa());
					pd.setPrecoCusto(it1.getPrecoCusto());
					lstProdutosAgrupados.add(pd);
				
				} else {
					Iterator<Produto> it2 = lstProdutosAgrupados.iterator();
					boolean jaExiste = false;
					while (it2.hasNext()) {
						Produto pd2 = it2.next();
						if (pd2.getCodigoProduto().compareTo(it1.getCodigoProduto()) == 0) {
							jaExiste = true;
							break;
						}
					}
					if (!jaExiste) {
						Produto pd = new Produto();
						pd.setCodigoProduto(it1.getCodigoProduto());
						pd.setDesconto(it1.getDesconto());
						pd.setEdicaoProduto(it1.getEdicaoProduto());
						pd.setNomeProduto(it1.getNomeProduto());
						pd.setPrecoCapa(it1.getPrecoCapa());
						pd.setPrecoCusto(it1.getPrecoCusto());
						lstProdutosAgrupados.add(pd);
						break;
					}
				}

			}

			Iterator<Produto> it = lstProdutosAgrupados.iterator();
			cbxListaProdutos.removeAllItems();
			while (it.hasNext()) {
				cbxListaProdutos.addItem(it.next());
			}
			
			iListLancamentos = lstLancamentos.iterator();
			lstCotas = new ArrayList<Cota>();
			while (iListLancamentos.hasNext()) {
				Lancamento it1 = iListLancamentos.next();
				
				if (lstCotas.size() == 0) {
					Cota ct = new Cota();
					ct.setCodigoCota(it1.getCodigoCota());
					ct.setCodLed(it1.getCodigoLed());
					lstCotas.add(ct);
				} else {
					Iterator<Cota> it2 = lstCotas.iterator();
					boolean jaExiste = false;
					while (it2.hasNext()) {
						Cota ct2 = it2.next();
						if (ct2.getCodigoCota().compareTo(it1.getCodigoCota()) == 0) {
							jaExiste = true;
							break;
						}
					}
					if (!jaExiste) {
						Cota ct = new Cota();
						ct.setCodigoCota(it1.getCodigoCota());
						ct.setCodLed(it1.getCodigoLed());
						lstCotas.add(ct);
						break;
					}
				}

			}

		} catch (CarregarLancamentoException e) {
			lblStatusBarMessage.setText(e.getMessage());
			// e.printStackTrace();
		}
	}
}
