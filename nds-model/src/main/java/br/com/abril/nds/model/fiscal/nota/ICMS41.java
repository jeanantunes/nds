package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlType;

@Embeddable
@XmlType(name="ICMS41")
public class ICMS41 extends ICMS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8282717732178224828L;

}