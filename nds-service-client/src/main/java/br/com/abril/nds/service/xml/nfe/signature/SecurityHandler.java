package br.com.abril.nds.service.xml.nfe.signature;

import org.w3c.dom.Element;

public interface SecurityHandler {
	
	public void handle(Element parentElement, Element elementToSign, SecurityCallBack action);

}