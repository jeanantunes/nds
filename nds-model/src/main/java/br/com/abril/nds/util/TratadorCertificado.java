package br.com.abril.nds.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TratadorCertificado {

	private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

	private static final String XML_SIGNATURE_TYPE    = "DOM";

	public static String assinar(String xml){
		String retorno = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			builder = factory.newDocumentBuilder();
			Document docs = builder.parse(new ByteArrayInputStream(xml.getBytes()));

			NodeList elements = docs.getElementsByTagName("infNFe");
			Element el = (Element) elements.item(0);
			el.setIdAttribute("Id", true);
			String id = el.getAttribute("Id");

			XMLSignatureFactory fac = XMLSignatureFactory.getInstance(XML_SIGNATURE_TYPE, new XMLDSigRI());
			TransformParameterSpec tps = null;
			Transform envelopedTransform = fac.newTransform(Transform.ENVELOPED, tps);
			Transform c14NTransform = fac.newTransform(C14N_TRANSFORM_METHOD, tps);

			ArrayList transformList = new ArrayList();
			transformList.add(envelopedTransform);
			transformList.add(c14NTransform);

			DigestMethod dm = fac.newDigestMethod(DigestMethod.SHA1, null);
			Reference ref = fac.newReference("#" + id, dm, transformList, null, null);
			CanonicalizationMethod cm = fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
					(C14NMethodParameterSpec) null);
			SignatureMethod sm = fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
			SignedInfo si = fac.newSignedInfo(cm, sm, Collections.singletonList(ref));
			KeyStore ks = KeyStore.getInstance("JKS");
			
			StringBuilder storeLocation = new StringBuilder(System.getProperty("java.home"));
	    	
	    	
	    	storeLocation.append("\\lib\\security"+"\\cacerts");
	    	
			FileInputStream fis = new FileInputStream("C://nfe/keystore/certificado.jks");
			char[] senha = "clientpass".toCharArray();
			ks.load(fis, senha);
			fis.close();
			Enumeration aliasesEnum = null;
			aliasesEnum = ks.aliases();
			String alias = "";
			while(aliasesEnum.hasMoreElements()){
				alias = (String) aliasesEnum.nextElement();
				if(ks.isKeyEntry(alias)){
					break;
				}
			}

			KeyStore.Entry entry = null;
			KeyStore.PasswordProtection kspp = new KeyStore.PasswordProtection(senha);
			entry = ks.getEntry(alias, kspp);
			KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) entry;
			X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			List x509Content = new ArrayList();

			x509Content.add(cert);
			X509Data xd = kif.newX509Data(x509Content);
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = null;
			ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
			doc = dbf.newDocumentBuilder().parse(bais);

			bais.close();

			Node node = doc.getElementsByTagName("NFe").item(0);
			DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), node);
			XMLSignature signature = fac.newXMLSignature(si, ki);
			signature.sign(dsc);

			OutputStream os = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = null;
			trans = tf.newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(os));
			retorno = os.toString();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return retorno;
	}

	public static void main(String[] args) {
		
		String content = null;
		try {
			content = new Scanner(new File("c://sergio.xml")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(content);

		
		assinar(content);
	} 
}
