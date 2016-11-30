package br.com.abril.nds.model.fiscal.nota;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PISNTWrapper {
	
	@XmlElement(name="PISNT")
	private PIS pis;
	
	public PIS getPis() {
		return pis;
	}

	public void setPis(PIS pis) {
		this.pis = pis;
	}

}
