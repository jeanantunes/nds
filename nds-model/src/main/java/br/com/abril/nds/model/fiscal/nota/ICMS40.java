package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlType;

@Embeddable
@AttributeOverrides({
		@AttributeOverride(name = "cst", column = @Column(name = "CST_ICMS", length = 2, nullable = false)),
		@AttributeOverride(name = "valorBaseCalculo", column = @Column(name = "VLR_BASE_CALC_ICMS", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "aliquota", column = @Column(name = "ALIQUOTA_ICMS", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "valor", column = @Column(name = "VLR_ICMS", precision = 5, scale = 2, nullable = true))

})
@XmlType(name="ICMS40")
public class ICMS40 extends ICMS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8282717732178224828L;

}