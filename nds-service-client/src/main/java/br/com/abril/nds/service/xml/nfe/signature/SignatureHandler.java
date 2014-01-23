package br.com.abril.nds.service.xml.nfe.signature;

import javax.xml.crypto.XMLStructure;

public interface SignatureHandler {
	
	public void sign(XMLStructure sourceStructure, String tagNameToSign) throws Exception;

}
