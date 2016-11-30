package br.com.abril.nds.model.fiscal.nota;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CofinsNTWrapper {
	
	@XmlElement(name="COFINSNT")
	private COFINS cofins;
	
	public COFINS getCofins() {
		return cofins;
	}

	public void setCofins(COFINS cofins) {
		this.cofins = cofins;
	}
}
