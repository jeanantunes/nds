package br.com.abril.ndsled.serialcom;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;

public class PortaCom {
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

	public PortaCom(String p, int b, int t) {
		this.NomePorta = p;
		this.baudrate = b;
		this.timeout = t;
	}

	public void ObterIdDaPorta() {

		try {
			cp = CommPortIdentifier.getPortIdentifier(NomePorta);
			if (cp == null) {
				System.out.println("Erro na porta");
				IDPortaOK = false;
				System.exit(1);
			}
			IDPortaOK = true;
		} catch (Exception e) {
			System.out.println("Erro obtendo ID da porta: " + e);
			IDPortaOK = false;
			System.exit(1);
		}

	}

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
			System.out.println("Erro abrindo comunicação: " + e);
			System.exit(1);
		}
	}

	public void EnviarComando(String msg) {
		try {
			saida = porta.getOutputStream();
			System.out.println("FLUXO OK!");
		} catch (Exception e) {
			System.out.println("Erro.STATUS: " + e);
		}
		try {
			System.out.println("Enviando um byte para " + NomePorta);
			System.out.println("Enviando : " + msg);
			saida.write(msg.getBytes());
			Thread.sleep(100);
			saida.flush();
		} catch (Exception e) {
			System.out.println("Houve um erro durante o envio. ");
			System.out.println("STATUS: " + e);
			System.exit(1);
		}
	}

	public void FecharCom() {
		try {
			porta.close();
		} catch (Exception e) {
			System.out.println("Erro fechando porta: " + e);
			System.exit(0);
		}
	}

	public String obterNomePorta() {
		return NomePorta;
	}

	public int obterBaudrate() {
		return baudrate;
	}
}
