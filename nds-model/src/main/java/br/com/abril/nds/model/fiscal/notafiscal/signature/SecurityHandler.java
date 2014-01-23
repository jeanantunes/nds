package br.com.abril.nds.model.fiscal.notafiscal.signature;

import org.w3c.dom.Element;

public interface SecurityHandler {
	
	public void handle(Element parentElement, Element elementToSign, SecurityCallBack action);

}
