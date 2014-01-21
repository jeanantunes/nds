package br.com.abril.nds.model.fiscal.notafiscal.signature;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.w3c.dom.Element;

public interface SecurityCallBack {

	public void doInSecurityContext(Element parentElement, Element elementToSign, Certificate certificate, PrivateKey privateKey);
	
}
