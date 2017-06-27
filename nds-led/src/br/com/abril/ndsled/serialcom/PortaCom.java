package br.com.abril.ndsled.serialcom;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PortaCom {

	// ================================================================================
	// Properties
	// ================================================================================
	Logger logger = LogManager.getLogger(PortaCom.class);
	public String Dadoslidos;
	public int nodeBytes;
	private int baudrate;
	private int timeout;
	private CommPortIdentifier cp;
	private SerialPort porta;
	private OutputStream saida;
	private boolean IDPortaOK;
	private boolean PortaOK;
	private String NomePorta;

	// ================================================================================
	// Constructors
	// ================================================================================
	/**
	 * Metodo construtor.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 * @param p
	 *            Nome da Porta COM
	 * @param b
	 *            Baudrate da Porta COM
	 * @param t
	 *            Timeout para a Porta COM
	 */
	public PortaCom(String p, int b, int t) {
		this.NomePorta = p;
		this.baudrate = b;
		this.timeout = t;
	}

	// ================================================================================
	// Methods
	// ================================================================================
	/**
	 * Obter ID da Porta Serial COM.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 */
	public void ObterIdDaPorta() {

		try {
			cp = CommPortIdentifier.getPortIdentifier(NomePorta);
			if (cp == null) {
				// System.out.println("Erro na porta");
				logger.error("Erro na porta");
				IDPortaOK = false;
				System.exit(1);
			}
			IDPortaOK = true;
		} catch (Exception e) {
			// System.out.println("Erro obtendo ID da porta: " + e);
			logger.error("Erro obtendo ID da porta: " + e);
			IDPortaOK = false;
			System.exit(1);
		}

	}

	/**
	 * Abre como comunicacao com a porta Serial COM.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 * 
	 */
	public void AbrirPorta() {
		try {
			porta = (SerialPort) cp.open("SerialPort", timeout);
			PortaOK = true;
			// configurar parâmetros
			porta.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			porta.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		} catch (Exception e) {
			PortaOK = false;
			// System.out.println("Erro abrindo comunicação: " + e);
			logger.error("Erro abrindo comunicação: " + e);
			System.exit(1);
		}
	}

	/**
	 * Envia o comando em bytes para a Serial COM.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 * @param msg
	 *            Comando para a porta
	 */
	public void EnviarComando(String msg) {
		try {
			saida = porta.getOutputStream();
			System.out.println("FLUXO OK!");
			logger.info("FLUXO OK!");
		} catch (Exception e) {
			System.out.println("Erro.STATUS: " + e);
			logger.error("Erro.STATUS: " + e);
		}
		try {
			System.out.println("Enviando um byte para " + NomePorta);
			System.out.println("Enviando : " + msg);
			logger.info("Enviando um byte para " + NomePorta);
			logger.info("Enviando : " + msg);
			saida.write(msg.getBytes());
			Thread.sleep(5);
			saida.flush();
		} catch (Exception e) {
			System.out.println("Houve um erro durante o envio. ");
			logger.error("Houve um erro durante o envio.");
			System.out.println("STATUS: " + e);
			logger.error("STATUS: " + e);
			System.exit(1);
		}
	}

	/**
	 * Fecha a comunicacao com a porta Serial COM
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 */
	public void FecharCom() {
		try {
			if(porta != null){
				porta.close();
			}
			else{
				logger.error("A porta COM ja estava fechada.");
			}
		} catch (Exception e) {
			System.out.println("Erro fechando porta: " + e);
			logger.error("Erro fechando porta: " + e);
			System.exit(0);
		}
	}

	/**
	 * Metodo de acesso para obter o nomen da Porta COM.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 * @return Nome da porta
	 */
	public String obterNomePorta() {
		return NomePorta;
	}

	/**
	 * Metodo de acesso para obter o Baudrate da Porta COM.
	 * 
	 * @author André W da Silva
	 * @since 19/07/2016
	 * @return Baudrate da porta
	 */
	public int obterBaudrate() {
		return baudrate;
	}
}
