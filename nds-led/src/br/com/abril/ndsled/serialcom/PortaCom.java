package br.com.abril.ndsled.serialcom;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.abril.ndsled.actions.AppActions;

public class PortaCom {
	
	//================================================================================
    // Properties
    //================================================================================
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

	//================================================================================
    // Constructors
    //================================================================================
	/**
	* @description Metodo construtor
	* @author André W da Silva
	* @date 19/07/2016
	* @access Privado
	* @param String Nome da Porta COM. 
	* @param int Baudrate da Porta COM.
	* @param int Timeout para a Porta COM.
	* @return nenhum
	*/
	public PortaCom(String p, int b, int t) {
		this.NomePorta = p;
		this.baudrate = b;
		this.timeout = t;
	}

	//================================================================================
    // Methods
    //================================================================================
	/**
	* @description Obter ID da Porta Serial COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Public
	*/
	public void ObterIdDaPorta() {

		try {
			cp = CommPortIdentifier.getPortIdentifier(NomePorta);
			if (cp == null) {
				//System.out.println("Erro na porta");
				logger.error("Erro na porta");
				IDPortaOK = false;
				System.exit(1);
			}
			IDPortaOK = true;
		} catch (Exception e) {
			//System.out.println("Erro obtendo ID da porta: " + e);
			logger.error("Erro obtendo ID da porta: " + e);
			IDPortaOK = false;
			System.exit(1);
		}

	}

	/**
	* @description Abre como comunicacao com a porta Serial COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Publico
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
			//System.out.println("Erro abrindo comunicação: " + e);
			logger.error("Erro abrindo comunicação: " + e);
			System.exit(1);
		}
	}
	
	/**
	* @description Envia o comando em bytes para a Serial COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Public
	* @param String msg - comando para a porta
	*/
	public void EnviarComando(String msg) {
		try {
			saida = porta.getOutputStream();
			//System.out.println("FLUXO OK!");
			logger.info("FLUXO OK!");
		} catch (Exception e) {
			//System.out.println("Erro.STATUS: " + e);
			logger.error("Erro.STATUS: " + e);
		}
		try {
			//System.out.println("Enviando um byte para " + NomePorta);
			//System.out.println("Enviando : " + msg);
			logger.info("Enviando um byte para " + NomePorta);
			logger.info("Enviando : " + msg);
			saida.write(msg.getBytes());
			Thread.sleep(100);
			saida.flush();
		} catch (Exception e) {
			//System.out.println("Houve um erro durante o envio. ");
			logger.error("Houve um erro durante o envio.");
			//System.out.println("STATUS: " + e);
			logger.error("STATUS: " + e);
			System.exit(1);
		}
	}

	/**
	* @description Fecha a comunicacao com a porta Serial COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Public
	*/
	public void FecharCom() {
		try {
			porta.close();
		} catch (Exception e) {
			//System.out.println("Erro fechando porta: " + e);
			logger.error("Erro fechando porta: " + e);
			System.exit(0);
		}
	}

	/**
	* @description Metodo de acesso para obter o nomen da Porta COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Public
	* @return String - nome da porta
	*/
	public String obterNomePorta() {
		return NomePorta;
	}

	/**
	* @description Metodo de acesso para obter o Baudrate da Porta COM
	* @author André W da Silva
	* @date 19/07/2016
	* @access Public
	* @return int - baudrate da porta
	*/
	public int obterBaudrate() {
		return baudrate;
	}
}
