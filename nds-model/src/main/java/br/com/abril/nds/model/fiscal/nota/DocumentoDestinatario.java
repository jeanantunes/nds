package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentoDestinatario {

	@XmlValue
	@Column(name="DOCUMENTO_DESTINATARIO", nullable=false, length=14)
	private String documento;

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
}