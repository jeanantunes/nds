package br.com.dgb.nfe;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;

import br.com.abril.nfe.integracao.NfeRecepcao2Stub;

public class NFeRecepcao {

	private static final int SSL_PORT = 443; 
    
	public static void main(String[] args) {
    	
    	
        try {
            String codigoDoEstado = "35";

            /**
             * Enderecos de Homoloção do Sefaz Virtual RS
             * para cada WebService existe um endereco Diferente.
             */
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
            URL url = new URL("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nferetrecepcao/NfeRetRecepcao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfecancelamento/NfeCancelamento2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeinutilizacao/NfeInutilizacao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeconsulta/NfeConsulta2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfestatusservico/NfeStatusServico2.asmx");
            
            String caminhoDoCertificadoDoCliente = "C:/Users/wrpaiva/certificadoAR/certificado.jks";  
            String senhaDoCertificado = "arutil14";  
            String arquivoCacertsGeradoTodosOsEstados = "NFeCacerts";  
  
            InputStream entrada = new FileInputStream(caminhoDoCertificadoDoCliente);  
            KeyStore ks = KeyStore.getInstance("pkcs12");  
            try {  
                ks.load(new FileInputStream("C:/Users/wrpaiva/certificadoAR/certificado.jks"), "arutil14".toCharArray());
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
  
            String alias = "";  
            Enumeration<String> aliasesEnum = ks.aliases();  
            while (aliasesEnum.hasMoreElements()) {  
                alias = (String) aliasesEnum.nextElement();  
                if (ks.isKeyEntry(alias)) {  
                    break;  
                }  
            }  
            X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);  
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());  
            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);  
            socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);  
  
            Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);  
            Protocol.registerProtocol("https", protocol);  
            
            /**
             * IMPORTANTE: O XML já deve ser assinado antes do envio.
             * Lendo o Xml de um arquivo Gerado.
             */
            StringBuilder xml = new StringBuilder();
            String linha = null;
            String caminhoArquivo = "C:/Users/wrpaiva/certificadoAR/NF-e-31-00000002.xml";
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8"));
            
            while ((linha = in.readLine()) != null) {
                xml.append(linha);
            }
            in.close();

            String xmlEnvNFe = xml.toString();
            OMElement ome = AXIOMUtil.stringToOM(xmlEnvNFe);

            Iterator<?> children = ome.getChildrenWithLocalName("NFe");  
            while (children.hasNext()) {
	            OMElement omElement = (OMElement) children.next();  
	            if ((omElement != null) && ("NFe".equals(omElement.getLocalName()))) {  
	            	omElement.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);  
	            }
            }

            NfeRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRecepcao2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);
            NfeRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRecepcao2Stub.NfeCabecMsg();
            /**
             * Código do Estado.
             */
            nfeCabecMsg.setCUF(codigoDoEstado);

            /**
             * Versao do XML
             */
            nfeCabecMsg.setVersaoDados("3.10");

            NfeRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRecepcao2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeRecepcao2Stub stub = new NfeRecepcao2Stub(url.toString());
            NfeRecepcao2Stub.NfeRecepcaoLote2Result result = stub.nfeRecepcaoLote2(dadosMsg, nfeCabecMsgE);

            System.out.println(result.getExtraElement().toString());
        } catch (Exception e) {
        	System.out.println("ERRO"+ e);
        	
            e.printStackTrace();
        }
    }

}