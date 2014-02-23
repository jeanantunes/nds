package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class Impostos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4845108096352563349L;
	
	@XmlElements({@XmlElement(name="ICMS41", type=ICMS41.class)})
	private ICMS icms;

	@XmlElement(name="IPI")
	private IPI ipi;

	public ICMS getIcms() {
		return icms;
	}

	public void setIcms(ICMS icms) {
		this.icms = icms;
	}

	public IPI getIpi() {
		return ipi;
	}

	public void setIpi(IPI ipi) {
		this.ipi = ipi;
	}
	
}