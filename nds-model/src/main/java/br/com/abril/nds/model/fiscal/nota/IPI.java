package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_IPI", length = 3, nullable = false)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_IPI", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_IPI", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_IPI", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="valorIsento", column=@Column(name="VLR_ISENTO_IPI", precision = 5, scale = 2)),
    @AttributeOverride(name="valorOutros", column=@Column(name="VLR_OUTROS_IPI", precision = 5, scale = 2)),
    @AttributeOverride(name="valorDebito", column=@Column(name="VLR_DEB_IPI", precision = 5, scale = 2)),
    @AttributeOverride(name="valorCredito", column=@Column(name="VLR_CRE_IPI", precision = 5, scale = 2)),
    @AttributeOverride(name="tipoBaseCalculo", column=@Column(name="TIP_BSC_IPI", length = 1))
})
public class IPI extends ImpostoProduto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6435493668780899303L;
	
	/**
	 * Construtor padrão.
	 */
	public IPI() {
		
	}

}
