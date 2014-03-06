package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Impostos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4845108096352563349L;

	@XmlElement(name="ICMS")
	private ICMSWrapper icms;

	@XmlElement(name="IPI")
	private IPI ipi;
	
	@XmlElement(name="PIS")
	private PIS pis;

	@XmlElement(name="COFINS")
	private COFINS cofins;
	
	public ICMS getIcms() {
		if(this.icms == null) return null;
		return icms.getIcms();
	}

	public void setIcms(ICMS icms) {
		if(this.icms == null) this.icms = new ICMSWrapper();
		this.icms.setIcms(icms);
	}

	public IPI getIpi() {
		return ipi;
	}

	public void setIpi(IPI ipi) {
		this.ipi = ipi;
	}

	public PIS getPis() {
		return pis;
	}

	public void setPis(PIS pis) {
		this.pis = pis;
	}

	public COFINS getCofins() {
		return cofins;
	}

	public void setCofins(COFINS cofins) {
		this.cofins = cofins;
	}
}