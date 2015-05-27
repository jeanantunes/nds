package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalReferenciadaWrapper implements Serializable {
	
	private static final long serialVersionUID = 2313535449916955751L;
	
	@OneToMany(mappedBy="pk.notaFiscal", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@XmlElements(value={ @XmlElement(name="NFref") })
	private List<NotaFiscalReferenciada> listReferenciadas;

	public List<NotaFiscalReferenciada> getListReferenciadas() {
		return listReferenciadas;
	}

	public void setListReferenciadas(List<NotaFiscalReferenciada> listReferenciadas) {
		this.listReferenciadas = listReferenciadas;
	}
}