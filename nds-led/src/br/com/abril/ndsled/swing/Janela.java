package br.com.abril.ndsled.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import br.com.abril.ndsled.actions.AppActions;
import br.com.abril.ndsled.modelo.Box;
import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.Produto;
import br.com.abril.ndsled.serialcom.PortaCom;
import br.com.abril.ndsled.serialcom.SerialCom;
import br.com.abril.ndsled.thread.EnviarLed;
import br.com.abril.ndsled.thread.FecharAplicacao;
import br.com.abril.ndsled.thread.LimparLed;

/**
 * Classe da Janela Principal do aplicativo
 * 
 * @author André W da Silva
 * @since 1.0
 */
public class Janela {

	// ================================================================================
	// Properties
	// ================================================================================
	Logger logger = LogManager.getLogger(Janela.class);
	private Properties props;
	private JFrame frmProjetoLedV;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mnitSair;
	private JPanel pnlConfiguracao;
	private JComboBox cbxPortaSerial;
	private JComboBox cbxListaProdutos;
	private JLabel lblData;
	private JLabel lblListaProdutos;
	private JLabel lblPortaSerial;
	private JLabel lblStatusBarMessage;
	private JButton btnEnviar;
	private JButton btnAcenderLeds;
	private JScrollPane pnlCotaLed;
	private JTable tblCotaLed;
	private JButton btnApagarLeds;
	private JDatePickerImpl pckDataLancamento;
	private List<Lancamento> lstLancamentos;
	private List<Produto> lstProdutosAgrupados;
	private List<Cota> lstCotas;
	private List<Box> lstBox;
	private JPanel pnlStatusBar;
	private JDatePanelImpl datePanel;
	private JLabel lblCaractereReparteZero;
	private JTextField txtCatactereReparteZero;
	private JScrollPane pnlBoxQuantidade;
	private JTable tblBoxQuantidade;
	private JButton btnAcenderCota;
	private JTextField txtCodigoDeBarras;
	private JLabel lblCodigoDeBarras;

	// ================================================================================
	// Constructors
	// ================================================================================
	public Janela() {
		initialize();

		SerialCom serial = new SerialCom();
		serial.ListarPortas();

		List<String> portas = serial.ObterPortas();
		Iterator<String> iPortas = portas.iterator();
		while (iPortas.hasNext()) {
			cbxPortaSerial.addItem(iPortas.next());
		}

		portas = null;
		iPortas = null;
		serial = null;
	}

	// ================================================================================
	// Accessors
	// ================================================================================
	public JFrame getFrmProjetoLedV() {
		return frmProjetoLedV;
	}

	// ================================================================================
	// Private Methods
	// ================================================================================
	/**
	 * Método utilizado para inicializar todos os objetos da Janela.
	 * 
	 * @author André W da Silva
	 * @since 1.0
	 * @param Nothing
	 * @return Nothing
	 */
	private void initialize() {
		logger.info("Iniciando Aplicativo.");

		try {
			props = AppActions.loadProperties(AppActions.class.getClassLoader()
					.getResourceAsStream("app.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		frmProjetoLedV = new JFrame();
		frmProjetoLedV
				.setTitle(props.getProperty("app.title") + " Distribuidor: "
						+ props.getProperty("app.cod_distribuidor"));
		frmProjetoLedV.setResizable(false);
		frmProjetoLedV.setBounds(100, 100, 920, 540);
		frmProjetoLedV.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		menuBar = new JMenuBar();
		frmProjetoLedV.setJMenuBar(menuBar);

		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		mnitSair = new JMenuItem("Sair");
		mnitSair.setSelectedIcon(null);
		mnMenu.add(mnitSair);
		frmProjetoLedV.getContentPane().setLayout(null);

		lblData = new JLabel("Data Lan\u00E7amento:");
		lblData.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblData.setBounds(10, 10, 277, 23);
		frmProjetoLedV.getContentPane().add(lblData);

		lblListaProdutos = new JLabel("Lista Produtos:");
		lblListaProdutos.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblListaProdutos.setBounds(10, 66, 277, 14);
		frmProjetoLedV.getContentPane().add(lblListaProdutos);

		cbxListaProdutos = new JComboBox();
		cbxListaProdutos.setFont(new Font("Tahoma", Font.BOLD, 14));

		cbxListaProdutos.setBounds(10, 91, 477, 20);
		frmProjetoLedV.getContentPane().add(cbxListaProdutos);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnEnviar.setBounds(151, 122, 131, 54);
		frmProjetoLedV.getContentPane().add(btnEnviar);

		pnlConfiguracao = new JPanel();
		pnlConfiguracao.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Configura\u00E7\u00E3o",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		pnlConfiguracao.setBounds(10, 214, 277, 240);
		frmProjetoLedV.getContentPane().add(pnlConfiguracao);
		pnlConfiguracao.setLayout(null);

		cbxPortaSerial = new JComboBox();
		cbxPortaSerial.setFont(new Font("Tahoma", Font.BOLD, 14));
		cbxPortaSerial.setBounds(10, 47, 131, 20);
		pnlConfiguracao.add(cbxPortaSerial);

		btnAcenderLeds = new JButton("Acender Leds");
		btnAcenderLeds.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAcenderLeds
				.setToolTipText("Acende todos com 8888 os Leds para verificar os luminosos.");
		btnAcenderLeds.setBounds(10, 78, 168, 34);
		pnlConfiguracao.add(btnAcenderLeds);

		lblPortaSerial = new JLabel("Porta Serial");
		lblPortaSerial.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPortaSerial.setBounds(10, 22, 142, 14);
		pnlConfiguracao.add(lblPortaSerial);
		lblPortaSerial.setHorizontalAlignment(SwingConstants.LEFT);

		btnApagarLeds = new JButton("Apagar Leds");
		btnApagarLeds.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnApagarLeds.setToolTipText("");
		btnApagarLeds.setBounds(10, 123, 168, 34);
		pnlConfiguracao.add(btnApagarLeds);

		lblCaractereReparteZero = new JLabel("Caractere Reparte Zero:");
		lblCaractereReparteZero.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCaractereReparteZero.setBounds(10, 184, 213, 14);
		pnlConfiguracao.add(lblCaractereReparteZero);

		txtCatactereReparteZero = new JTextField();
		txtCatactereReparteZero.setFont(new Font("Tahoma", Font.BOLD, 16));

		txtCatactereReparteZero.setText(" ");
		txtCatactereReparteZero.setBounds(10, 209, 22, 20);
		pnlConfiguracao.add(txtCatactereReparteZero);
		txtCatactereReparteZero.setColumns(10);

		pnlCotaLed = new JScrollPane();
		pnlCotaLed.setBounds(583, 6, 313, 448);
		frmProjetoLedV.getContentPane().add(pnlCotaLed);

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		datePanel = new JDatePanelImpl(new UtilDateModel(), p);
		pckDataLancamento = new JDatePickerImpl(datePanel,
				new DateLabelFormatter());

		SpringLayout springLayout = (SpringLayout) pckDataLancamento
				.getLayout();
		springLayout.putConstraint(SpringLayout.SOUTH,
				pckDataLancamento.getJFormattedTextField(), 0,
				SpringLayout.SOUTH, pckDataLancamento);
		pckDataLancamento.getJFormattedTextField().setFont(
				new Font("Tahoma", Font.PLAIN, 16));
		pckDataLancamento.setBounds(10, 35, 175, 25);
		frmProjetoLedV.getContentPane().add(pckDataLancamento);

		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		pnlStatusBar.setBounds(0, 465, 914, 25);
		frmProjetoLedV.getContentPane().add(pnlStatusBar);
		pnlStatusBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

		lblStatusBarMessage = new JLabel("");
		pnlStatusBar.add(lblStatusBarMessage);

		tblCotaLed = new JTable(new Object[][] { { "", "", "" } },
				new String[] { "Cota", "Reparte", "Led" });
		tblCotaLed.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tblCotaLed.setBounds(0, 0, 100, 100);
		pnlCotaLed.setViewportView(tblCotaLed);

		tblCotaLed
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tblCotaLed.setRowSelectionAllowed(false);
		tblCotaLed.setModel(new DefaultTableModel(new Object[][][] {},
				new String[] { "Cota", "Reparte", "Led" }) {
			Class[] columnTypes = new Class[] { Integer.class, Integer.class,
					Integer.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblCotaLed.getColumnModel().getColumn(0).setResizable(false);
		tblCotaLed.getColumnModel().getColumn(1).setResizable(false);
		tblCotaLed.getColumnModel().getColumn(2).setResizable(false);
		tblCotaLed
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		btnAcenderCota = new JButton(
				"<html><center>Acender<br>Cota</center></html>");
		btnAcenderCota.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnAcenderCota.setBounds(10, 122, 131, 54);
		frmProjetoLedV.getContentPane().add(btnAcenderCota);

		pnlBoxQuantidade = new JScrollPane();
		pnlBoxQuantidade.setBounds(311, 122, 250, 332);
		frmProjetoLedV.getContentPane().add(pnlBoxQuantidade);

		tblBoxQuantidade = new JTable();
		tblBoxQuantidade.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tblBoxQuantidade.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "BOX", "Quantidade" }));
		tblBoxQuantidade.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
				null));
		pnlBoxQuantidade.setViewportView(tblBoxQuantidade);

		txtCodigoDeBarras = new JTextField();

		txtCodigoDeBarras.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtCodigoDeBarras.setBounds(233, 35, 200, 25);
		frmProjetoLedV.getContentPane().add(txtCodigoDeBarras);
		txtCodigoDeBarras.setColumns(10);

		lblCodigoDeBarras = new JLabel("C\u00F3digo de Barras");
		lblCodigoDeBarras.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCodigoDeBarras.setBounds(233, 10, 277, 23);
		frmProjetoLedV.getContentPane().add(lblCodigoDeBarras);

		// ================================================================================
		// Events
		// ================================================================================
		txtCatactereReparteZero.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtCatactereReparteZero.setText(AppActions.maxlength(
						txtCatactereReparteZero.getText(), 1));
			}
		});

		mnitSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LimparLed limparLed = new LimparLed(txtCatactereReparteZero,
						cbxPortaSerial, lstCotas, lblStatusBarMessage);
				limparLed.start();
				FecharAplicacao fecharAplicacao = new FecharAplicacao(
						limparLed, lblStatusBarMessage);
				fecharAplicacao.start();
			}
		});

		cbxListaProdutos.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				carregaTblBoxQuantidade(((Produto) cbxListaProdutos
						.getSelectedItem()).getCodigoProduto(),
						((Produto) cbxListaProdutos.getSelectedItem())
								.getEdicaoProduto());
				if (lstCotas != null) {
					carregaTblCotaLed(((Produto) cbxListaProdutos
							.getSelectedItem()).getCodigoProduto(),
							((Produto) cbxListaProdutos.getSelectedItem())
									.getEdicaoProduto());
				}
				txtCodigoDeBarras.requestFocus();
			}
		});

		btnApagarLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String charZerado = " ";
				if (!txtCatactereReparteZero.getText().isEmpty()) {
					charZerado = txtCatactereReparteZero.getText();
				}

				// Iniciando leitura serial.
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while (cotaIterator.hasNext()) {
						String codLed = String.format("%04d", cotaIterator
								.next().getCodLed());
						pCom.EnviarComando("p" + codLed + charZerado
								+ charZerado + charZerado + charZerado);
						Thread.sleep(50);
					}
					Thread.sleep(50);
					cotaIterator = null;
					pCom.FecharCom();
				} catch (Exception ex) {
					// System.out.println(ex.getMessage());
					logger.error(ex.getMessage());
				}
				txtCodigoDeBarras.requestFocus();
			}
		});

		btnAcenderLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial.
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while (cotaIterator.hasNext()) {
						String codLed = String.format("%04d", cotaIterator
								.next().getCodLed());
						pCom.EnviarComando("p" + codLed + "8888");
						Thread.sleep(50);
					}
					Thread.sleep(50);
					cotaIterator = null;
					pCom.FecharCom();
				} catch (Exception ex) {
					// System.out.println(ex.getMessage());
					logger.error(ex.getMessage());
				}
				txtCodigoDeBarras.requestFocus();
			}
		});

		btnAcenderCota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial.
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while (cotaIterator.hasNext()) {
						Cota cota = cotaIterator.next();
						String codLed = String.format("%04d", cota.getCodLed());
						String codCota = String.format("%04d",
								cota.getCodigoCota());
						pCom.EnviarComando("p" + codLed + codCota);
						Thread.sleep(50);
					}
					Thread.sleep(50);
					cotaIterator = null;
					pCom.FecharCom();
				} catch (Exception ex) {
					// System.out.println(ex.getMessage());
					logger.error(ex.getMessage());
				}
				txtCodigoDeBarras.requestFocus();
			}
		});

		pckDataLancamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarLancamento((Date) pckDataLancamento.getModel()
						.getValue());
			}
		});

		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EnviarLed enviarLed = new EnviarLed(cbxPortaSerial,
						cbxListaProdutos, lstLancamentos,
						txtCatactereReparteZero, lstCotas, lblStatusBarMessage,
						btnEnviar, txtCodigoDeBarras, lstProdutosAgrupados);
				enviarLed.start();
				txtCodigoDeBarras.requestFocus();
			}
		});

		frmProjetoLedV.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LimparLed limparLed = new LimparLed(txtCatactereReparteZero,
						cbxPortaSerial, lstCotas, lblStatusBarMessage);
				limparLed.start();
				FecharAplicacao fecharAplicacao = new FecharAplicacao(
						limparLed, lblStatusBarMessage);
				fecharAplicacao.start();
			}
		});

		txtCodigoDeBarras.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println(e.getKeyCode());
				if (e.getKeyCode() == 10) {
					try {
						Produto produto = AppActions.getProdutoByCodBarras(
								txtCodigoDeBarras.getText(),
								lstProdutosAgrupados);
						cbxListaProdutos.setSelectedItem(produto);
						btnEnviar.doClick();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						logger.error(ex.getMessage());
					}
				}
			}
		});

		desabilitarObjetosView();
	}

	/**
	 * Metodo local utilizado para carregar o lancamento por data, Atribui todos
	 * os dados carregados do couchDB para os objetos da tela.
	 * 
	 * @author André W da Silva
	 * @since 1.0
	 * @param date
	 *            Deve vir da data selecionada no calendario.
	 */
	private void carregarLancamento(Date date) {
		try {
			// Reset carregamentos anteriores.
			Thread.sleep(50);
			try {
				limparObjetosView();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}

			Thread.sleep(50);
			try {
				limparObjetosView();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}

			String charZerado = " ";
			if (!txtCatactereReparteZero.getText().isEmpty()) {
				charZerado = txtCatactereReparteZero.getText();
			}

			// Iniciando leitura serial.
			try {
				if (lstCotas != null) {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while (cotaIterator.hasNext()) {
						String codLed = String.format("%04d", cotaIterator
								.next().getCodLed());
						pCom.EnviarComando("p" + codLed + charZerado
								+ charZerado + charZerado + charZerado);
						Thread.sleep(5);
					}
					Thread.sleep(5);
					pCom.FecharCom();
					cotaIterator = null;
				}
			} catch (Exception ex) {
				// System.out.println(ex.getMessage());
				logger.error(ex.getMessage());
			}

			lstLancamentos = AppActions.carregarLancamento(date);

			// Agrupa em Lista os Produtos que estao no Lancamento.
			Iterator<Lancamento> iListLancamentos = lstLancamentos.iterator();
			lstProdutosAgrupados = new ArrayList<Produto>();
			while (iListLancamentos.hasNext()) {
				Lancamento it1 = iListLancamentos.next();

				if (lstProdutosAgrupados.size() == 0) {
					Produto pd = new Produto();
					pd.setCodigoProduto(it1.getCodigoProduto());
					pd.setDesconto(it1.getDesconto());
					pd.setEdicaoProduto(it1.getEdicaoProduto());
					pd.setNomeProduto(it1.getNomeProduto().trim());
					pd.setPrecoCapa(it1.getPrecoCapa());
					pd.setPrecoCusto(it1.getPrecoCusto());
					pd.setQuantidade(it1.getQuantidadeReparte());
					pd.setCodigoBarras(it1.getCodigoBarras());
					lstProdutosAgrupados.add(pd);
				} else {
					Iterator<Produto> it2 = lstProdutosAgrupados.iterator();
					boolean jaExiste = false;
					while (it2.hasNext()) {
						Produto pd2 = it2.next();
						if (pd2.getCodigoProduto().compareTo(
								it1.getCodigoProduto()) == 0
								&& pd2.getEdicaoProduto().compareTo(
										it1.getEdicaoProduto()) == 0) {
							pd2.setQuantidade(pd2.getQuantidade()
									+ it1.getQuantidadeReparte());
							jaExiste = true;
							break;
						}
					}
					if (!jaExiste) {
						Produto pd = new Produto();
						pd.setCodigoProduto(it1.getCodigoProduto());
						pd.setDesconto(it1.getDesconto());
						pd.setEdicaoProduto(it1.getEdicaoProduto());
						pd.setNomeProduto(it1.getNomeProduto().trim());
						pd.setPrecoCapa(it1.getPrecoCapa());
						pd.setPrecoCusto(it1.getPrecoCusto());
						pd.setQuantidade(it1.getQuantidadeReparte());
						pd.setCodigoBarras(it1.getCodigoBarras());
						lstProdutosAgrupados.add(pd);
					}
					it2 = null;
				}
				it1 = null;
			}

			// Ordena a lista de produtos agrupados por nome.
			Collections.sort(lstProdutosAgrupados, new Comparator<Produto>() {
				@Override
				public int compare(Produto o1, Produto o2) {
					return o1.getNomeProduto().compareToIgnoreCase(
							o2.getNomeProduto());
				}
			});

			// Alimenta o Combobox de Produtos com a lista de produtos agrupado
			// do dia selecionado.
			Iterator<Produto> itListProdutosAgrupados = lstProdutosAgrupados
					.iterator();

			while (itListProdutosAgrupados.hasNext()) {
				Produto prd = itListProdutosAgrupados.next();
				cbxListaProdutos.addItem(prd);
			}

			itListProdutosAgrupados = null;

			// Filtra em Lista as Cotas que estao no Lancamento.
			iListLancamentos = lstLancamentos.iterator();
			lstCotas = new ArrayList<Cota>();
			while (iListLancamentos.hasNext()) {
				Lancamento it1 = iListLancamentos.next();

				if (lstCotas.size() == 0) {
					Cota ct = new Cota(it1.getCodigoCota(), it1.getCodigoLed(),
							0);
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
					it2 = null;
					if (!jaExiste) {
						Cota ct = new Cota(it1.getCodigoCota(),
								it1.getCodigoLed(), 0);
						lstCotas.add(ct);
					}
				}
				it1 = null;
			}

			// Verificar Cota Sem Led.
			if (AppActions.verificarCotaSemLed(lstCotas)) {
				lblStatusBarMessage.setText("Verificar: Existe Cotas sem Led.");
				desabilitarObjetosView();
			} else {
				// Exibe mensagem na Barra de Status.
				lblStatusBarMessage.setText("Lançamento "
						+ new SimpleDateFormat("dd/MM/yyyy").format(date)
						+ " carregado.");
				habilitarObjetosView();
			}

			carregaTblBoxQuantidade(
					((Produto) cbxListaProdutos.getSelectedItem())
							.getCodigoProduto(),
					((Produto) cbxListaProdutos.getSelectedItem())
							.getEdicaoProduto());
			carregaTblCotaLed(
					((Produto) cbxListaProdutos.getSelectedItem())
							.getCodigoProduto(),
					((Produto) cbxListaProdutos.getSelectedItem())
							.getEdicaoProduto());

			iListLancamentos = null;

		} catch (Exception e) {
			logger.error(e.getMessage());
			lblStatusBarMessage.setText(e.getMessage());
			desabilitarObjetosView();
		}
	}

	/**
	 * Metodo utilizado para carregar a JTable com os dados da Cota do dia pelo
	 * Produto selecionado.
	 * 
	 * @param codigoProdutoSelecionado
	 *            codigo do produto selecionado
	 * @param edicaoProdutoSelecionado
	 *            edicao do produto selecionado
	 * @author Andre W Silva
	 * @since 1.0
	 */
	private void carregaTblCotaLed(Integer codigoProdutoSelecionado,
			Integer edicaoProdutoSelecionado) {

		// Limpa a Tabela (JTable) com a Cotas/LEDs de outros dados carregados.
		DefaultTableModel dtm = (DefaultTableModel) tblCotaLed.getModel();
		for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
			dtm.removeRow(i);
		}

		// Filtra em Lista as Cotas para a JTable Cota Led do produto
		// selecionado.
		Iterator<Cota> iListCotas = lstCotas.iterator();
		while (iListCotas.hasNext()) {
			Cota cota = iListCotas.next();
			cota.setReparte(getQuantidadeReparteProdutoCota(
					codigoProdutoSelecionado, edicaoProdutoSelecionado,
					cota.getCodigoCota()));
		}

		// Ordena a lista de Cotas pelo numero da Cota.
		Collections.sort(lstCotas, new Comparator<Cota>() {
			@Override
			public int compare(Cota o1, Cota o2) {
				return o1.getCodigoCota().compareTo(o2.getCodigoCota());
			}
		});

		// Alimenta a Tabela (JTable) com a Cota/Led do Lancamento do dia
		// pelo produto selecionado.
		iListCotas = lstCotas.iterator();
		while (iListCotas.hasNext()) {
			Cota cota = iListCotas.next();
			dtm.addRow(new Object[] { cota.getCodigoCota(), cota.getReparte(),
					cota.getCodLed() });
		}
		iListCotas = null;
	}

	/**
	 * Metodo utilizado para carregar a JTable com os dados do lancamento do dia
	 * pelo produto selecionado com codigo do box e quantidade.
	 * 
	 * @author Andre W Silva
	 * @since 1.0
	 */
	private void carregaTblBoxQuantidade(Integer codigoProdutoSelecionado,
			Integer edicaoProdutoSelecionado) {
		// Limpa a Tabela (JTable) com a Box/Quantidade de outros dados
		// carregados.
		DefaultTableModel dtm = (DefaultTableModel) tblBoxQuantidade.getModel();
		for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
			dtm.removeRow(i);
		}

		// Filtra em Lista os BOX que estao no Lancamento pelo produto
		// selecionado.
		Iterator<Lancamento> iListLancamentos = lstLancamentos.iterator();
		lstBox = new ArrayList<Box>();

		while (iListLancamentos.hasNext()) {
			Lancamento it1 = iListLancamentos.next();

			if (it1.getCodigoProduto().compareTo(codigoProdutoSelecionado) == 0
					&& it1.getEdicaoProduto().compareTo(
							edicaoProdutoSelecionado) == 0) {
				if (lstBox.size() == 0) {
					Box box = new Box();
					box.setCodigoBox(it1.getCodigoBox());
					box.setQuantidade(it1.getQuantidadeReparte());
					lstBox.add(box);
				} else {
					Iterator<Box> it2 = lstBox.iterator();
					boolean jaExiste = false;
					while (it2.hasNext()) {
						Box box2 = it2.next();
						if (box2.getCodigoBox().compareTo(it1.getCodigoBox()) == 0) {
							box2.setQuantidade(box2.getQuantidade()
									+ it1.getQuantidadeReparte());
							jaExiste = true;
							break;
						}
					}
					it2 = null;
					if (!jaExiste) {
						Box box = new Box();
						box.setCodigoBox(it1.getCodigoBox());
						box.setQuantidade(it1.getQuantidadeReparte());
						lstBox.add(box);
					}
				}
			}
			it1 = null;
		}

		// Ordena a lista de box pelo numero da box.
		Collections.sort(lstBox, new Comparator<Box>() {
			@Override
			public int compare(Box o1, Box o2) {
				return o1.getCodigoBox().compareTo(o2.getCodigoBox());
			}
		});

		// Alimenta a Tabela (JTable) com a Box/Quantidade do Lancamento do dia
		// pelo produto selecionado.
		Iterator<Box> itListBox = lstBox.iterator();
		while (itListBox.hasNext()) {
			Box box = itListBox.next();
			dtm.addRow(new Object[] { box.getCodigoBox(), box.getQuantidade() });
		}
		itListBox = null;
		iListLancamentos = null;
	}

	/**
	 * Metodo utliziado para desabilitar os objetos da tela que não devem ser
	 * manipulado quando um lançamento não foi carregado.
	 * 
	 * @author Andre W Silva
	 * @since 1.0
	 */
	private void desabilitarObjetosView() {
		btnEnviar.setEnabled(false);
		btnAcenderLeds.setEnabled(false);
		btnApagarLeds.setEnabled(false);
		btnAcenderCota.setEnabled(false);
		pnlConfiguracao.setEnabled(false);
		txtCodigoDeBarras.setEnabled(false);
	}

	/**
	 * Metodo utliziado para habilitar os objetos da tela que devem ser
	 * manipulado quando um lancamento foi carregado.
	 * 
	 * @author Andre W Silva
	 * @since 1.0
	 */
	private void habilitarObjetosView() {
		btnEnviar.setEnabled(true);
		btnAcenderLeds.setEnabled(true);
		btnApagarLeds.setEnabled(true);
		btnAcenderCota.setEnabled(true);
		pnlConfiguracao.setEnabled(true);
		txtCodigoDeBarras.setEnabled(true);
		txtCodigoDeBarras.requestFocus();
	}

	/**
	 * Metodo utilizado para limpar os dados dos objetos quando o dia do
	 * lancamento e alterado.
	 * 
	 * @author Andre W Silva
	 * @since 1.0
	 */
	private void limparObjetosView() {
		// Limpa Barra de Status.
		lblStatusBarMessage.setText("");
		// Limpa Combobox de Produtos.
		cbxListaProdutos.removeAllItems();

		// Limpa a Tabela (JTable) com a Cotas/LEDs de outros dados carregados.
		DefaultTableModel dtm = (DefaultTableModel) tblCotaLed.getModel();
		for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
			dtm.removeRow(i);
		}
		// Limpa a Tabela (JTable) com a Box / Quantidade.
		dtm = (DefaultTableModel) tblBoxQuantidade.getModel();
		for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
			dtm.removeRow(i);
		}
	}

	/**
	 * Metodo utilizado para obter a quantidade de reparte do produto para uma
	 * cota.
	 * 
	 * @param codigoProduto
	 * @param edicaoProduto
	 * @param codigoCota
	 * @return Integer - Quantidade de Reparte para Cota
	 * @author t40080 Andre
	 * @since 1.0
	 */
	private Integer getQuantidadeReparteProdutoCota(Integer codigoProduto,
			Integer edicaoProduto, Integer codigoCota) {
		Iterator<Lancamento> iListLancamentos = lstLancamentos.iterator();

		while (iListLancamentos.hasNext()) {
			Lancamento it1 = iListLancamentos.next();

			if (it1.getCodigoProduto().compareTo(codigoProduto) == 0
					&& it1.getEdicaoProduto().compareTo(edicaoProduto) == 0
					&& it1.getCodigoCota().compareTo(codigoCota) == 0) {
				return it1.getQuantidadeReparte();
			}
			it1 = null;
		}
		iListLancamentos = null;
		return 0;
	}
}
