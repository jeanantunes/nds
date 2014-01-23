package br.com.abril.nds.service.xml.nfe.signature;

import java.security.PrivateKey;
import java.security.cert.Certificate;
   
public interface SignatureBuilder<T> {
	
	public void build(T elementToSign, T parentElement, Certificate certificate, PrivateKey privateKey);
      
}
