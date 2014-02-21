package br.com.abril.nds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TratadorCertificado {

	/*
	 * 	Classe para tratamento de certificados. Deve fazer a manipulacao
	 * dos certificados exportando chaves, assinando XML's e demais funcoes.
	 *
	 */	
	public static final String algoritmo= "RSA"; 
	public static final String algoritmoAssinatura= "MD5withRSA"; 
	private static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	public static File file= new File("C://certificado.cer"); // ‪C:\certificado.cer
	private static String alias="sefaz";
	private static char[] senha="changeit".toCharArray(); 
	static XMLSignatureFactory sig;
	static X509Certificate cert;
	static KeyInfo ki; 
	static SignedInfo si;
	static KeyStore rep;
	
	public static PrivateKey getChavePrivada() throws Exception{
				
		InputStream entrada= new FileInputStream(file);
		rep.load(entrada, senha);
		entrada.close();
		Key chavePrivada= (Key) rep.getKey(alias, senha);
		if(chavePrivada instanceof PrivateKey){
			System.out.println("Chave Privada encontrada!");
			return (PrivateKey) chavePrivada;
		}
		return null;		
	}
	
	public static PublicKey getChavePublica() throws Exception{
			
		InputStream entrada= new FileInputStream(file);
		rep.load(entrada, senha);
		entrada.close();
		Key chave= (Key) rep.getKey(alias, senha);		
		java.security.Certificate cert= (java.security.Certificate) rep.getCertificate(alias);//O tipo de dado é declarado desse modo por haver ambigüidade (Classes assinadas com o mesmo nome "Certificate")
		PublicKey chavePublica= cert.getPublicKey();
		System.out.println("Chave Pública encontrada!");
		return chavePublica;		
	}
	
	public static boolean verificarAssinatura(PublicKey chave, byte[] buffer, byte[] assinado) throws Exception{
		
		Signature assinatura= Signature.getInstance(algoritmoAssinatura);
		assinatura.initVerify(chave);
		assinatura.update(buffer, 0, buffer.length);
		return assinatura.verify(assinado);
	}
	
	public static byte[] criarAssinatura(PrivateKey chavePrivada, byte[] buffer) throws Exception{
		
		Signature assinatura= Signature.getInstance(algoritmoAssinatura);		
		assinatura.initSign(chavePrivada);
		assinatura.update(buffer, 0, buffer.length);
		return assinatura.sign();
	}
	
	public static String getValidade(X509Certificate cert){
		try{
			cert.checkValidity();
			return "Certificado válido!";
		}
		catch(CertificateExpiredException e){
			return "Certificado expirado!";
		}
		catch(CertificateNotYetValidException e){
			return "Certificado inválido!";
		}		
	}	
	
	public static void getCertificado() throws Exception{					
		InputStream dado= new FileInputStream(file);		
		rep= KeyStore.getInstance("cert");		
		rep.load(dado, senha);
		cert= (X509Certificate) rep.getCertificate(alias);
		String retorno= TratadorCertificado.getValidade(cert);
		System.out.println(retorno);		
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
		
		DOMSignContext dsc = new DOMSignContext
	    (getChavePrivada(), doc.getDocumentElement());
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream("nfeassinada.xml");
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		
	}
	
	public static void main(String[] args) {
		try{
			TratadorCertificado.getCertificado();			
			TratadorCertificado.assinarDocumento("C:\\Users\\wrpaiva\\Desktop\\receita\\xml\\sergioValidar3.xml");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
