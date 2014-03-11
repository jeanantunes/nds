package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ICMSWrapper extends ICMSBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2313535449916955751L;
	
	@XmlElements({
		@XmlElement(name="ICMS40", type=ICMS40.class),
		@XmlElement(name="ICMS40", type=ICMS41.class)
	})
	private ICMS icms;

	public ICMS getIcms() {
		return icms;
	}

	public void setIcms(ICMS icms) {
		this.icms = icms;
	}

}
