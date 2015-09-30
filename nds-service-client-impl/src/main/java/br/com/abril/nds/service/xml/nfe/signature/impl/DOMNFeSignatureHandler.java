package br.com.abril.nds.service.xml.nfe.signature.impl;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.xml.nfe.signature.SecurityCallBack;
import br.com.abril.nds.service.xml.nfe.signature.SecurityHandler;
import br.com.abril.nds.service.xml.nfe.signature.SignatureBuilder;
import br.com.abril.nds.service.xml.nfe.signature.SignatureHandler;

@Component
public class DOMNFeSignatureHandler implements SignatureHandler {

	private static final Logger logger = LoggerFactory.getLogger(DOMNFeSignatureHandler.class);
	
	@Autowired
	private SecurityHandler securityHandler;
	
	@Autowired
	private SignatureBuilder<Element> signatureBuilder;
	
	public void sign(XMLStructure sourceStructure, String tagNameToSign) throws Exception {
		Element sourceElement = (Element) ((DOMStructure) sourceStructure).getNode();
		sourceElement.normalize();
		logger.debug("Pronto para extrair {} para assinatura de {}.", tagNameToSign, sourceElement);
		NodeList elementsToSign = sourceElement.getElementsByTagName(tagNameToSign);
		if (elementsToSign!=null && elementsToSign.getLength()>0) {
			for (int i = 0; i < elementsToSign.getLength(); i++) {
				Element elementToSign = (Element) elementsToSign.item(i);
				Element parentElement = (Element) elementToSign.getParentNode();
				logger.debug("Pronto para invocar 'securityHandler' para assinar {} #{}.", elementToSign.getTagName(), i);
				securityHandler.handle(parentElement, elementToSign , new SecurityCallBack() {
					public void doInSecurityContext(Element parentElement, Element elementToSign, Certificate certificate, PrivateKey privateKey) {
						signatureBuilder.build(elementToSign, parentElement, certificate, privateKey);
					}
				});
			}
		} else {
			logger.warn("Não foi encontrada a tag {} para assinar.", tagNameToSign);
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrada a tag {} para assinar.");
			
		}
	}
}
