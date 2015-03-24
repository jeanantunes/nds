package br.com.abril.nds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SecurityUtils {
	
    private final static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    static char SEP = File.separatorChar;
    static String trustStoreName = "cacerts";
    static String trustStorePath = null;
    static String trustStorePassword = "arutil14";
    
    public static final String algoritmo= "RSA"; 
	public static final String algoritmoAssinatura= "MD5withRSA"; 
	private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	public static File file= new File("C://Users/wrpaiva/certificadoAR/certificado.jks"); // ‪C:\certificado.cer
	private static String alias="alias";
	private static char[] senha="changeit".toCharArray(); 
	static XMLSignatureFactory sig;
	static X509Certificate cert;
	static KeyInfo ki; 
	static SignedInfo si;
	static KeyStore rep;

	private static PrivateKey privateKey;
    
    public static KeyStore openStore(String keyStoreType, Resource keyStoreResource, char[] passphrase) throws Exception {
    	logger.debug("Abrindo armazém {} ...", keyStoreResource.getFilename());
    	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    	keyStore.load(keyStoreResource.getInputStream(), passphrase);
    	return keyStore;
    }
    
    public static KeyStore openStore(Resource keyStoreResource, char[] passphrase) throws Exception {
    	return openStore(KeyStore.getDefaultType(), keyStoreResource, passphrase);
    }

    public static KeyStore openStore(String keyStoreType, String storeLocation, char[] passphrase) throws Exception {
    	return openStore(keyStoreType, new FileSystemResource(storeLocation), passphrase);
    }

    public static KeyStore openStore(String storeLocation, char[] passphrase) throws Exception {
    	return openStore(new FileSystemResource(storeLocation), passphrase);
    }
    
    public static KeyStore openTrustStore(char[] passphrase) throws Exception {
    	return openTrustStore(trustStorePath, passphrase);
    }
    
    public static KeyStore openTrustStore(String trustStorePath, char[] passphrase) throws Exception {
    	StringBuilder storeLocation = new StringBuilder(System.getProperty("java.home"));
    	if (trustStorePath==null) {
        	storeLocation.append(SEP)
		    .append("lib")
		    .append(SEP)
			.append("security")
			.append(SEP);
    	}
    	else {
    		storeLocation.append(trustStorePath+"\\");
    	}
    	KeyStore trustStore = SecurityUtils.openStore(storeLocation.append(trustStoreName).toString(), passphrase);
    	return trustStore;
    }
    
    public static void installCertificate(String certificateLocation, String certificateName) throws Exception {
    	installCertificate(trustStorePath, certificateLocation, certificateName);
    }
    
    public static void installCertificate(String trustStorePath, String certificateLocation, String certificateName) throws Exception {
    	KeyStore trustStore = SecurityUtils.openTrustStore(trustStorePath, trustStorePassword.toCharArray());
	    File dir = new File(certificateLocation);
	    File file = new File(dir, certificateName+".cer");
	    
	    logger.debug("Abrindo certificado {} ...", file);
    	
	    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    	InputStream in = new FileInputStream(file);
    	cert = (X509Certificate) cf.generateCertificate(in);
    	
    	assinarDocumento("C:\\Users\\wrpaiva\\Desktop\\receita\\xml\\sergioValidar3.xml");
    	in.close();
    	
    	if (trustStore.containsAlias(certificateName)) {
    		logger.info("Certificado existente {}", trustStore.getCertificate(certificateName).getType());
    	}
    	
    	else {
    		trustStore.setCertificateEntry(certificateName, cert);
    		logger.info("Certificado CARREGADO {}", trustStore.getCertificate(certificateName).getType());
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	String trustStorePath = null;
    	String certificateLocation = "";
    	String certificateName = "";
		if (args.length > 2) {
			trustStorePath = args[2];
			certificateLocation = args[1];
			certificateName = args[0];
		} 
		else if (args.length > 1) {
			certificateLocation = args[1];
			certificateName = args[0];
		}
		else {
		    System.out.println("Uso: java SecurityUtils <local do certificado> <nome do certificado> [local do cacerts a partir do JAVAHOME]");
		    return;
		}
		installCertificate(trustStorePath, certificateLocation, certificateName);
		
		
    }
    
    
    public static void assinarDocumento(String localDocumento) throws Exception{		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(localDocumento));
		System.out.println("Documento ok!");
		
		sig= XMLSignatureFactory.getInstance("DOM");
		
		ArrayList<Transform> transformList= new ArrayList<Transform>();
		Transform enveloped= sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n= sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		
		NodeList elements = doc.getElementsByTagName("infNFe");
		org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);
		String id = el.getAttribute("Id");
		Reference r= sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
					(C14NMethodParameterSpec) null),
						sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
						Collections.singletonList(r)						
		);
		
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();		
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));		
		
		DOMSignContext dsc = new DOMSignContext(getChavePrivada(), doc.getDocumentElement());
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream("nfeassinada.xml");
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		
	}
    
    public static PrivateKey getChavePrivada() throws Exception{
    	Certificate certificate = null;
    	
    	KeyStore ksKeys = KeyStore.getInstance("JKS");
		ksKeys.load(new FileInputStream("C://Users/wrpaiva/Desktop/receita/certificado.jks"), "clientpass".toCharArray());
		ksKeys.getKey(alias, "changeit".toCharArray());
		Enumeration<String> aliases = ksKeys.aliases();
		if (aliases.hasMoreElements()) {
			String transportAlias = aliases.nextElement();
			certificate = ksKeys.getCertificate(transportAlias);
			logger.debug("Certificado: {}.", ((X509Certificate) certificate).getSubjectDN());
			return privateKey = (PrivateKey) ksKeys.getKey(transportAlias, senha);
		} else {
			throw new IllegalArgumentException("Armazém principal não cont�m um certificado válido.");
		}	
	}
}
