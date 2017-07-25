package br.com.abril.ndsled.serialcom;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SerialCom {

	//protected String[] portas;
	private List<String> portas;
	
	protected Enumeration listaDePortas;

	public SerialCom() {
		listaDePortas = CommPortIdentifier.getPortIdentifiers();
	}

	public List<String> ObterPortas() {
		return portas;
	}

	public void ListarPortas() {
		int i = 0;
		portas = new ArrayList<String>();
		//portas = new String[10];
		while (listaDePortas.hasMoreElements()) {
			CommPortIdentifier ips = (CommPortIdentifier) listaDePortas
					.nextElement();

			if (ips.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portas.add(ips.getName());
				i++;
			}
		}
	}

	public boolean PortaExiste(String COMp) {
		String temp;
		boolean e = false;
		while (listaDePortas.hasMoreElements()) {
			CommPortIdentifier ips = (CommPortIdentifier) listaDePortas
					.nextElement();
			temp = ips.getName();
			if (temp.equals(COMp) == true) {
				e = true;
			}
		}
		return e;
	}

}
