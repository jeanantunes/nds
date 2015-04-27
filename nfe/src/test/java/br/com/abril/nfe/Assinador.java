package br.com.abril.nfe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Assinador {

	public static void main(String[] args) {

		String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
		String PROVIDER_CLASS_NAME = "org.jcp.xml.dsig.internal.dom.XMLDSigRI";
		String PROVIDER_NAME = "jsr105Provider";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File("C:\\Users\\wrpaiva\\certificadoAR\\teste.xml"));
			NodeList elements = doc.getElementsByTagName("infNFe");
			Element el = (Element) elements.item(0);
			String idNota = el.getAttribute("Id");
			el.setIdAttribute("Id", true);

			System.out.println("ID: " + idNota);

			String providerName = System.getProperty(PROVIDER_NAME, PROVIDER_CLASS_NAME);

			XMLSignatureFactory factorySignature = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

			ArrayList transformList = new ArrayList();
			TransformParameterSpec transParamSpec = null;

			Transform envelopedTransform = factorySignature.newTransform(Transform.ENVELOPED, transParamSpec);
			Transform c14NTransform = factorySignature.newTransform(C14N_TRANSFORM_METHOD, transParamSpec);

			transformList.add(envelopedTransform);
			transformList.add(c14NTransform);

			Reference ref = factorySignature.newReference("#" + idNota, factorySignature.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);

			SignedInfo signedInfo = factorySignature.newSignedInfo(factorySignature.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
					factorySignature.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream("C:\\Users\\wrpaiva\\certificadoAR\\certificado.pfx"), "arsp15".toCharArray());

			Enumeration aliasesEnum = ks.aliases();
			String alias = "";
			while (aliasesEnum.hasMoreElements()) {
				alias = (String) aliasesEnum.nextElement();
				System.out.println(alias);
				break;// encontrou um certificado
			}

			KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection("arutil14".toCharArray()));

			X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

			KeyInfoFactory factoryKeyInfo = factorySignature.getKeyInfoFactory();
			
			List x509Content = new ArrayList();

			x509Content.add(cert);

			X509Data x509Data = factoryKeyInfo.newX509Data(x509Content);

			KeyInfo keyInfo = factoryKeyInfo.newKeyInfo(Collections.singletonList(x509Data));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setNamespaceAware(true);

			// Document doc = dbf.newDocumentBuilder().parse(new
			// FileInputStream("nota.xml") );

			DOMSignContext domSignContext = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());

			XMLSignature signature = factorySignature.newXMLSignature(signedInfo, keyInfo);

			signature.sign(domSignContext);

			// /gera arquivo assinado
			OutputStream out = new FileOutputStream(new File("C:\\opt\\parametros_nds\\notas\\exportado\\NF-e-007-XITAO.xml"));
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer trans = transformFactory.newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(out));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
