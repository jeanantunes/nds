package br.com.abril.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Security;
import java.util.Scanner;

public class AssinaturaTest {

	public static void main(String[] args) {
		
		String senha = "changeit";

		String nfeCabecMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<cabecMsg xmlns=\"http://www.portalfiscal.inf.br/nfe\" "
			+ "versao=\"1.02\">" + "<versaoDados>1.07</versaoDados>"
			+ "</cabecMsg>";

		String nfeDadosMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<consStatServ " + " versao=\"1.07\""
			+ " xmlns=\"http://www.portalfiscal.inf.br/nfe\">"
			+ "<tpAmb>2</tpAmb>" + "<cUF>42</cUF>"
			+ "<xServ>STATUS</xServ>" + "</consStatServ>";

		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.setProperty("javax.net.ssl.keyStore","/home/sergio/Dropbox/DGB/NDS/Modelagem/NF-e/Certificados/certificado.pfx");
		System.setProperty("javax.net.ssl.keyStorePassword", "associacao");

		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", "/home/sergio/Dropbox/DGB/NDS/Modelagem/NF-e/Certificados/certificado.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "xxx");  

		/*
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("/home/sergio/Dropbox/DGB/NDS/Modelagem/NF-e/Certificados/certificado.cert"), senha.toCharArray());
        Enumeration aliasesEnum = ks.aliases();
        String alias = "";
        while (aliasesEnum.hasMoreElements()) {
            alias = (String) aliasesEnum.nextElement();

            if (ks.isKeyEntry(alias)) {
                System.out.println(alias);
                break;
            }
        }


        KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(senha.toCharArray()));
        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        */
        
		String content = null;
		try {
			content = new Scanner(new File("C://Users/wrpaiva/Desktop/receita/xml/sergioValidar3.xml")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//System.out.println(content);
		
        Assinatura.assinar(content);
		
	}

}