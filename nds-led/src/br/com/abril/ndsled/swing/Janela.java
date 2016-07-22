package br.com.abril.ndsled.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.crypto.spec.DESedeKeySpec;
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
import javax.swing.ListSelectionModel;
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

/**
* Classe da Janela Principal do aplicativo
* 
* @author André W da Silva
* @since 19/07/2016
*/
public class Janela {

	//================================================================================
    // Properties
    //================================================================================
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
	private JPanel pnlStatusBar;
	private JDatePanelImpl datePanel;

	//================================================================================
    // Constructors
    //================================================================================
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

	//================================================================================
    // Accessors
    //================================================================================
	public JFrame getFrmProjetoLedV() {
		return frmProjetoLedV;
	}
	
	//================================================================================
    // Private Methods
    //================================================================================
	/**
	* Método utilizado para inicializar todos os objetos da Janela.
	* 
	* @author André W da Silva
	* @since 19/07/2016
	* @param Nothing
	* @return Nothing
	*/
	private void initialize() {
		frmProjetoLedV = new JFrame();
		frmProjetoLedV.setTitle("Projeto LED v0.1");
		frmProjetoLedV.setResizable(false);
		frmProjetoLedV.setBounds(100, 100, 536, 387);
		frmProjetoLedV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		frmProjetoLedV.setJMenuBar(menuBar);

		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		mnitSair = new JMenuItem("Sair");
		mnitSair.setSelectedIcon(null);
		mnMenu.add(mnitSair);
		frmProjetoLedV.getContentPane().setLayout(null);

		lblData = new JLabel("Data Lan\u00E7amento:");
		lblData.setBounds(10, 10, 277, 14);
		frmProjetoLedV.getContentPane().add(lblData);

		lblListaProdutos = new JLabel("Lista Produtos:");
		lblListaProdutos.setBounds(10, 64, 277, 14);
		frmProjetoLedV.getContentPane().add(lblListaProdutos);

		cbxListaProdutos = new JComboBox();

		cbxListaProdutos.setBounds(10, 83, 277, 20);
		frmProjetoLedV.getContentPane().add(cbxListaProdutos);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(198, 114, 89, 23);
		frmProjetoLedV.getContentPane().add(btnEnviar);

		pnlConfiguracao = new JPanel();
		pnlConfiguracao.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Configura\u00E7\u00E3o",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		pnlConfiguracao.setBounds(10, 147, 155, 158);
		frmProjetoLedV.getContentPane().add(pnlConfiguracao);
		pnlConfiguracao.setLayout(null);

		cbxPortaSerial = new JComboBox();
		cbxPortaSerial.setBounds(10, 47, 131, 20);
		pnlConfiguracao.add(cbxPortaSerial);

		btnAcenderLeds = new JButton("Acender Leds");
		btnAcenderLeds
				.setToolTipText("Acende todos com 8888 os Leds por 10 segundos para verificar os luminosos.");
		btnAcenderLeds.setBounds(10, 78, 131, 23);
		pnlConfiguracao.add(btnAcenderLeds);

		lblPortaSerial = new JLabel("Porta Serial");
		lblPortaSerial.setBounds(10, 22, 89, 14);
		pnlConfiguracao.add(lblPortaSerial);
		lblPortaSerial.setHorizontalAlignment(SwingConstants.LEFT);

		btnApagarLeds = new JButton("Apagar Leds");
		btnApagarLeds.setToolTipText("");
		btnApagarLeds.setBounds(10, 112, 131, 23);
		pnlConfiguracao.add(btnApagarLeds);

		pnlCotaLed = new JScrollPane();
		pnlCotaLed.setBounds(313, 11, 207, 294);
		frmProjetoLedV.getContentPane().add(pnlCotaLed);

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		datePanel = new JDatePanelImpl(new UtilDateModel(), p);
		pckDataLancamento = new JDatePickerImpl(datePanel,
				new DateLabelFormatter());
		pckDataLancamento.setBounds(10, 30, 126, 25);
		frmProjetoLedV.getContentPane().add(pckDataLancamento);

		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		pnlStatusBar.setBounds(0, 313, 530, 25);
		frmProjetoLedV.getContentPane().add(pnlStatusBar);
		pnlStatusBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

		lblStatusBarMessage = new JLabel("");
		pnlStatusBar.add(lblStatusBarMessage);

		tblCotaLed = new JTable(new Object[][] { { "", "" } }, new String[] {
				"Cota", "Led" });
		tblCotaLed.setBounds(0, 0, 100, 100);
		pnlCotaLed.setViewportView(tblCotaLed);

		tblCotaLed.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tblCotaLed.setRowSelectionAllowed(false);
		tblCotaLed.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Cota", "Led" }) {
			Class[] columnTypes = new Class[] { Integer.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblCotaLed.getColumnModel().getColumn(0).setResizable(false);
		tblCotaLed.getColumnModel().getColumn(1).setResizable(false);
		tblCotaLed.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		//================================================================================
	    // Events
	    //================================================================================
		mnitSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		cbxListaProdutos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Produto produtoSelecionado = (Produto) cbxListaProdutos
						.getSelectedItem();
				// System.out.println(produtoSelecionado);
				// System.out.println(produtoSelecionado.getCodigoCota());
				// System.out.println(produtoSelecionado.getQuantidadeReparte());
			}
		});
		
		btnApagarLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while(cotaIterator.hasNext()){
						String codLed = String.format("%04d", cotaIterator.next().getCodLed());
						pCom.EnviarComando("p"+codLed+"0000");
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
					Thread.sleep(100);
					pCom.FecharCom();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		btnAcenderLeds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Iniciando leitura serial
				try {
					PortaCom pCom = new PortaCom(cbxPortaSerial
							.getSelectedItem().toString(), 9600, 0);
					pCom.ObterIdDaPorta();
					pCom.AbrirPorta();
					Iterator<Cota> cotaIterator = lstCotas.iterator();
					while(cotaIterator.hasNext()){
						String codLed = String.format("%04d", cotaIterator.next().getCodLed());
						pCom.EnviarComando("p"+codLed+"8888");
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
					Thread.sleep(100);
					pCom.FecharCom();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
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
				enviarLed();
			}
		});
	}

	/**
	* Método local utilizado para carregar o lançamento por data,
	* Atriu todos os dados carregados do couchDB para os objetos da tela.
	* 
	* @author André W da Silva
	* @since 19/07/2016
	* @param date Java.util.Date deve vir da data selecionada no calendario. 
	* @return Nothing
	*/
	private void carregarLancamento(Date date) {
		try {
			
			//Reset carregamentos anteriores
			limparObjetosView();
			
			lstLancamentos = AppActions.carregarLancamento(date);  

			//Agrupa em Lista os Produtos que estão no Lançamento
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
						if (pd2.getCodigoProduto().compareTo(
								it1.getCodigoProduto()) == 0) {
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
					}
				}

			}
			
			//Ordena a lista de produtos adrupados por nome
			Collections.sort(lstProdutosAgrupados, new Comparator<Produto>() {

				@Override
				public int compare(Produto o1, Produto o2) {
					// TODO Auto-generated method stub
					return o1.getNomeProduto().compareToIgnoreCase(o2.getNomeProduto());
				}
			});
			
			//Alimenta o Combobox de Produtos com a lista de produtos agrupado do dia selecionado
			Iterator<Produto> itListProdutosAgrupados = lstProdutosAgrupados.iterator();
			
			while (itListProdutosAgrupados.hasNext()) {
				Produto prd = itListProdutosAgrupados.next();
				cbxListaProdutos.addItem(prd);	
			}
			

			//Filtra em Lista as Cotas que estão no Lançamento
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
					}
				}
			}
			
			//Ordena a lista de cota por numero da cota.
			Collections.sort(lstCotas, new Comparator<Cota>() {

				@Override
				public int compare(Cota o1, Cota o2) {
					// TODO Auto-generated method stub
					return o1.getCodigoCota().compareTo(o2.getCodigoCota());
				}
			});
			
			//Alimenta a Tabela (JTable)  com a Cotas/LEDs do Lançamento do dia selecionado.
			DefaultTableModel dtm = (DefaultTableModel) tblCotaLed.getModel();
			Iterator<Cota> itListCota = lstCotas.iterator();
			while (itListCota.hasNext()) {
				Cota cota = itListCota.next();
				dtm.addRow(new Object[] { cota.getCodigoCota(),
						cota.getCodLed() });
			}
			
			//Verificar Cota Sem Led
			if(AppActions.verificarCotaSemLed(lstCotas)){
				lblStatusBarMessage.setText("Verificar: Existe Cotas sem Led.");
				desabilitarObjetosView();
			}
			else{
				//Exibe mensagem na Barra de Status.
				lblStatusBarMessage.setText("Lançamento " + new SimpleDateFormat("dd/MM/yyyy").format(date) + " carregado.");
				habilitarObjetosView();
			}
			

		} catch (Exception e) {
			lblStatusBarMessage.setText(e.getMessage());
			desabilitarObjetosView();
		}
	}
	
	private void desabilitarObjetosView(){
		btnEnviar.setEnabled(false);
		btnAcenderLeds.setEnabled(false);
		btnApagarLeds.setEnabled(false);
		pnlConfiguracao.setEnabled(false);
	}
	
	private void habilitarObjetosView(){
		btnEnviar.setEnabled(true);
		btnAcenderLeds.setEnabled(true);
		btnApagarLeds.setEnabled(true);
		pnlConfiguracao.setEnabled(true);
	}
	
	private void limparObjetosView(){
		//Limpa Barra de Status
		lblStatusBarMessage.setText("");
		//Limpa Combobox de Produtos
		cbxListaProdutos.removeAllItems();
		//Limpa a Tabela (JTable) com a Cotas/LEDs de outros dados carregados
		DefaultTableModel dtm = (DefaultTableModel) tblCotaLed.getModel();
		for(int i = dtm.getRowCount()-1; i>=0;i--){
			dtm.removeRow(i);
		}
	}
	
	private void enviarLed(){
		
		PortaCom pCom = new PortaCom(cbxPortaSerial
				.getSelectedItem().toString(), 9600, 0);
		pCom.ObterIdDaPorta();
		pCom.AbrirPorta();
		
		Iterator<Cota> cotaIterator = lstCotas.iterator();
		while(cotaIterator.hasNext()){
			String codLed = String.format("%04d", cotaIterator.next().getCodLed());
			pCom.EnviarComando("p"+codLed+"0000");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Produto produtoSelecionado = (Produto) cbxListaProdutos
				.getSelectedItem();
		Iterator<Lancamento> iLanc = lstLancamentos.iterator();
		while(iLanc.hasNext()){
			Lancamento lanc = iLanc.next();
			if(lanc.getCodigoProduto().compareTo(produtoSelecionado.getCodigoProduto()) == 0){
				String codLed = String.format("%04d", lanc.getCodigoLed());
				String qtde = String.format("%04d", lanc.getQuantidadeReparte());
				pCom.EnviarComando("p"+codLed+qtde);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		pCom.FecharCom();
	}
}
