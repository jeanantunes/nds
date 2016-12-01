package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Impostos implements Serializable {

	private static final long serialVersionUID = 4845108096352563349L;

	@XmlElement(name="ICMS")
	private ICMSWrapper icms;

	@XmlElement(name="IPI")
	private IPI ipi;
	
	@XmlElement(name="PIS")
	private PISWrapper pis;

	@XmlElement(name="PIS")
	private PISOutrWrapper pisOutr;
	
	@XmlElement(name="PIS")
	private PISNTWrapper pisNT;
	
	@XmlElement(name="COFINS")
	private CofinsWrapper cofins;
	
	@XmlElement(name="COFINS")
	private CofinsOutrWrapper cofinsOutr;
	
	@XmlElement(name="COFINS")
	private CofinsNTWrapper cofinsNT;
	
	public ICMS getIcms() {
		if(this.icms == null) return null;
		return icms.getIcms();
	}

	public ICMSST getIcmsst() {
		if(this.icms == null) return null;
		return icms.getIcmsst();
	}
	
	public void setIcms(ICMS icms) {
		if(this.icms == null) this.icms = new ICMSWrapper();
		this.icms.setIcms(icms);
	}
	
	public void setIcms(ICMSST icms) {
		if(this.icms == null) this.icms = new ICMSWrapper();
		this.icms.setIcmsst(icms);
	}

	public IPI getIpi() {
		return ipi;
	}

	public void setIpi(IPI ipi) {
		this.ipi = ipi;
	}

	public PISWrapper getPis() {
		return pis;
	}

	public void setPis(PISWrapper pis) {
		this.pis = pis;
	}
	
	public PISOutrWrapper getPisOutr() {
		return pisOutr;
	}

	public void setPisOutr(PISOutrWrapper pisOutr) {
		this.pisOutr = pisOutr;
	}

	public PISNTWrapper getPisNT() {
		return pisNT;
	}

	public void setPisNT(PISNTWrapper pisNT) {
		this.pisNT = pisNT;
	}
	
	public CofinsWrapper getCofins() {
		return cofins;
	}

	public void setCofins(CofinsWrapper cofins) {
		this.cofins = cofins;
	}

	public CofinsOutrWrapper getCofinsOutr() {
		return cofinsOutr;
	}

	public void setCofinsOutr(CofinsOutrWrapper cofinsOutr) {
		this.cofinsOutr = cofinsOutr;
	}
	
	public CofinsNTWrapper getCofinsNT() {
		return cofinsNT;
	}

	public void setCofinsNT(CofinsNTWrapper cofinsNT) {
		this.cofinsNT = cofinsNT;
	}
}