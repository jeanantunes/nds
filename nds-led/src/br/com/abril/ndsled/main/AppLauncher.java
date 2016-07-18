package br.com.abril.ndsled.main;

import java.awt.EventQueue;

import br.com.abril.ndsled.swing.Janela;

public class AppLauncher {

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// TODO Auto-generated method stub
	// System.out.println("Hello World!");

	// SerialCom serial = new SerialCom();
	// serial.ListarPortas();

	// List<String> portas = serial.ObterPortas();
	// String[] portas = serial.ObterPortas();
	// Iterator<String> iPortas = portas.iterator();
	// while(iPortas.hasNext()){
	// System.out.println(iPortas.next());
	// }

	// Iniciando leitura serial
	// SerialComLeitura leitura = new SerialComLeitura("COM5", 9600, 0);
	// leitura.HabilitarEscrita();
	// leitura.ObterIdDaPorta();
	// leitura.AbrirPorta();
	// leitura.EnviarUmaString("p00020102");
	// leitura.FecharCom();

	// Controle de tempo da leitura aberta na serial
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException ex) {
	// System.out.println("Erro na Thread: " + ex);
	// }
	// }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela window = new Janela();
					window.getFrmProjetoLedV().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
