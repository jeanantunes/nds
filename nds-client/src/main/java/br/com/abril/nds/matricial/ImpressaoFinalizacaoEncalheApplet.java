package br.com.abril.nds.matricial;


import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JApplet;

import org.apache.log4j.Logger;

import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.util.ImpressaoMatricialUtil;
import br.com.abril.nds.util.ImpressoraUtil;

import com.itextpdf.text.pdf.codec.Base64;


public class ImpressaoFinalizacaoEncalheApplet extends JApplet{
    
    /**
     * 
     */
    private static final long serialVersionUID = -8547020123982005758L;
    private static final Logger LOGGER = Logger.getLogger(ImpressaoFinalizacaoEncalheApplet.class);


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

					if(conteudoImpressao != null){
						
						if(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name().equalsIgnoreCase(tipo_documento_impressao_encalhe)){
							
                            // Impressão matricial
							new ImpressaoMatricialUtil(new StringBuffer(conteudoImpressao)).imprimir(ImpressoraUtil.getImpressoraLocalMatricialNomePadrao());
							
						}else{
							
                            // Impressão laser
							new ImpressoraUtil().imprimirRPCEstrategia(Base64.decode(conteudoImpressao), ImpressoraUtil.getImpressoraLocalNaoMatricialNomePadrao());
						}
					}

				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				return accessibleContext;
			}
		});

	}
}