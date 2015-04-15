package br.com.abril.nds.service.xml.nfe.signature;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.w3c.dom.Element;

public interface SecurityCallBack {

	public void doInSecurityContext(Element parentElement, Element elementToSign, Certificate certificate, PrivateKey privateKey);
    	
}
