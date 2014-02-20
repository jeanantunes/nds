package br.com.abril.nds.service.xml.nfe.signature.impl;

import java.security.cert.Certificate;
import java.util.Collections;

import javax.annotation.Resource;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.abril.nds.service.xml.nfe.signature.KeyInfoBuilder;

public class DOMNFeKeyInfoBuilder implements KeyInfoBuilder {
    
	private static final Logger logger = LoggerFactory.getLogger(DOMNFeKeyInfoBuilder.class);
	private XMLSignatureFactory signatureFactory;
	
	/**
	 * METODO 
	 */
	
	public KeyInfo newKeyInfo(Certificate certificate) {
		KeyInfoFactory kif = signatureFactory.getKeyInfoFactory();
		X509Data x509Data = kif.newX509Data(Collections.singletonList(certificate));
		KeyInfo keyInfo = kif.newKeyInfo(Collections.singletonList(x509Data));
		logger.debug("Elemento <KeyInfo ...> preparado para assinatura.");
		return keyInfo;
	}

	@Resource
	protected void setXMLSignatureFactory(XMLSignatureFactory signatureFactory) {
		this.signatureFactory = signatureFactory;
	}	
}
