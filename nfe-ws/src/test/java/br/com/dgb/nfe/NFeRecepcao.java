package br.com.dgb.nfe;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.com.abril.nfe.integracao.NfeRecepcao2Stub;

public class NFeRecepcao {

    public static void main(String[] args) {
        try {
            String codigoDoEstado = "35";

            /**
             * Enderecos de Homoloção do Sefaz Virtual RS
             * para cada WebService existe um endereco Diferente.
             */
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
            URL url = new URL("https://nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nferetrecepcao/NfeRetRecepcao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfecancelamento/NfeCancelamento2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeinutilizacao/NfeInutilizacao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeconsulta/NfeConsulta2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfestatusservico/NfeStatusServico2.asmx");
            
            String caminhoDoCertificadoDoCliente = "C:\\Users\\wrpaiva\\certificadoAR\\certificadodigitalarsp2014.pfx";
            String senhaDoCertificadoDoCliente = "arutil14";
            String arquivoCacertsGeradoParaCadaEstado = "C:\\Users\\wrpaiva\\certificadoAR\\certificado.jks";

            /**
             * Informações do Certificado Digital.
             */
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            System.clearProperty("javax.net.ssl.keyStore");
            System.clearProperty("javax.net.ssl.keyStorePassword");
            System.clearProperty("javax.net.ssl.trustStore");

            System.setProperty("javax.net.ssl.keyStore", caminhoDoCertificadoDoCliente);
            System.setProperty("javax.net.ssl.keyStorePassword", senhaDoCertificadoDoCliente);

            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoParaCadaEstado);

            /**
             * IMPORTANTE: O XML já deve ser assinado antes do envio.
             * Lendo o Xml de um arquivo Gerado.
             */
            StringBuilder xml = new StringBuilder();
            String linha = null;
            String caminhoArquivo = "C:/opt/parametros_nds/notas/exportado/NF-e-0-00000001.xml";
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
            e.printStackTrace();
        }
    }

}