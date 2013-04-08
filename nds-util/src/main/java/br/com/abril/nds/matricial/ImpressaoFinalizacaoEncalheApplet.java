package br.com.abril.nds.matricial;


import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JApplet;


public class ImpressaoFinalizacaoEncalheApplet extends JApplet{

	// ========== - Metodo de iniciação do applet - =============
	@Override
	public void init() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					
					String parameter = getParameter("parameter");
					if(parameter != null)
						new EmissorNotaFiscalMatricial(new StringBuffer(parameter)).imprimir();

				} catch (Exception e) {
					e.printStackTrace();
				}
				return accessibleContext;
			}
		});

	}
}