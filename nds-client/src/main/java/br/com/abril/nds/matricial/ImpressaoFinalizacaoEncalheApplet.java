package br.com.abril.nds.matricial;


import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JApplet;

import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.util.ImpressaoMatricialUtil;
import br.com.abril.nds.util.ImpressoraUtil;


public class ImpressaoFinalizacaoEncalheApplet extends JApplet{

	// ========== - Metodo de iniciação do applet - =============
	@Override
	public void init() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					
					String tipo_documento_impressao_encalhe = getParameter("tipo_documento_impressao_encalhe");
					String conteudoImpressao = getParameter("conteudoImpressao");
					
					System.out.println("###>>>>>tipo_documento_impressao_encalhe = "+tipo_documento_impressao_encalhe+" <<<<<<<#########");
					System.out.println("###>>>>>conteudoImpressao = "+conteudoImpressao+" <<<<<<<#########");
					
					if(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name().equalsIgnoreCase(tipo_documento_impressao_encalhe)){
						
						//Impressão matricial
						if(conteudoImpressao != null)
							new ImpressaoMatricialUtil(new StringBuffer(conteudoImpressao)).imprimir(ImpressoraUtil.getImpressoraLocalMatricialNomePadrao());
					}else{
						
						//Impressão laser
						new ImpressoraUtil().imprimir(conteudoImpressao.getBytes(), ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return accessibleContext;
			}
		});

	}
}