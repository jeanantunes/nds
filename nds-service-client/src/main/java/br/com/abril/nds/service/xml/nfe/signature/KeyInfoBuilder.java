package br.com.abril.nds.service.xml.nfe.signature;

import java.security.cert.Certificate;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

public interface KeyInfoBuilder {
	
	public KeyInfo newKeyInfo(Certificate certificate);
	      
}
