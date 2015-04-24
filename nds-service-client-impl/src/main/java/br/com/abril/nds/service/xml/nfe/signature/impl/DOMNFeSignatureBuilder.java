package br.com.abril.nds.service.xml.nfe.signature.impl;

import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
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
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import br.com.abril.nds.service.xml.nfe.signature.KeyInfoBuilder;
import br.com.abril.nds.service.xml.nfe.signature.SignatureBuilder;

@Component
public class DOMNFeSignatureBuilder implements SignatureBuilder<Element>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(DOMNFeSignatureBuilder.class);
	
	public static final String CANONICALIZATION_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	public static final String C14N_TRANSFORMATION_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	
	private List<Transform> transformList;
	
	private DigestMethod digestMethod;
	
	@Autowired
	private XMLSignatureFactory signatureFactory;
	
	@Autowired
	private KeyInfoBuilder keyInfoBuilder;
	
	public DOMNFeSignatureBuilder() {
	
	}
	
	public DigestMethod getDigestMethod() {
		return digestMethod;
	}
	public void setDigestMethod(DigestMethod digestMethod) {
		this.digestMethod = digestMethod;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (signatureFactory==null) {
			// signatureFactory = newSignatureFactory();
		}
		if (transformList==null) {
			transformList = newTransformList();
		}
		if (logger.isDebugEnabled()) {
			StringBuilder debugMsg = new StringBuilder("Lista de transformacoes inclui algoritmos: ");
			for (Transform transform: transformList) {
				debugMsg.append(transform.getAlgorithm()).append(" ");
			}
			logger.debug(debugMsg.toString());
		}
		if (digestMethod==null) {
			digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
		}
		logger.debug("Algoritmo do metodo digest {}", digestMethod.getAlgorithm());
	}
	
	protected List<Transform> newTransformList() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		List<Transform> transformList = new ArrayList<Transform>();
		TransformParameterSpec tps = null;
		transformList.add(signatureFactory.newTransform(Transform.ENVELOPED, tps));
		transformList.add(signatureFactory.newTransform(C14N_TRANSFORMATION_METHOD, tps));
		return transformList;
	}
	
	public void build(Element elementToSign, Element parentElement, Certificate certificate, PrivateKey privateKey) {
		try {
			List<Reference> refList = newReferenceList(elementToSign);
			SignedInfo signedInfo = newSignedInfo(refList);
			KeyInfo keyInfo = keyInfoBuilder.newKeyInfo(certificate);
			DOMSignContext dsc = new DOMSignContext(privateKey, parentElement);
			XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
			logger.debug(" Primeiro digest value encontrado  {}", ((Reference) signature.getSignedInfo().getReferences().get(0)).getDigestValue());
			signature.sign(dsc);
			//logger.debug(" Primeiro digest value encontrado  {}", ((DOMReference) signature.getSignedInfo().getReferences().get(0)).getHere());
			InputStreamReader isr = new InputStreamReader(signature.getSignedInfo().getCanonicalizedData());
			char[] cbuf = new char[1024];
			
			while (isr.read(cbuf, 0, 1024) != -1) {
			    System.out.print(cbuf);
			}
			
			logger.debug(" Elemento <{}> assinado e inserido em <{}>.", elementToSign.getTagName(), parentElement.getTagName());
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossivel construir assinatura digital:", e);
		}
	}
	
	protected List<Reference> newReferenceList(Element elementToSign) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		String URI = new StringBuilder("#").append(elementToSign.getAttribute("Id")).toString();
		Reference ref = newReference(URI);
		ArrayList<Reference> refList = new ArrayList<Reference>();
		refList.add(ref);
		return refList;
	}

	protected Reference newReference(String URI) {
		Reference reference = signatureFactory.newReference(URI, digestMethod, transformList, null, null);
		logger.debug("Elemento <Reference URI='{}'.../>[{}] preparado para assinatura.", URI, reference.getDereferencedData());
		return reference;
	}
	
	protected SignedInfo newSignedInfo(List<Reference> refList) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		CanonicalizationMethod canonicalMethod = signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null);
		SignatureMethod sm = signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
		SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalMethod, sm, refList);
		logger.debug("Elemento <SignedInfo .../> preparado para assinatura.");
		return signedInfo;
	}
}
