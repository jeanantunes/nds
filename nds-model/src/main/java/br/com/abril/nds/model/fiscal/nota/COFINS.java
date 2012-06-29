package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_COFINS", length = 2)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_COFINS",  scale = 2,precision = 15)),
    @AttributeOverride(name="percentualAliquota", column=@Column(name="PER_ALIQ_COFINS", scale = 2, precision = 5)),
    @AttributeOverride(name="quantidadeVendida", column=@Column(name="QTD_VENDIDA_COFINS", scale = 4,  precision= 16)),
    @AttributeOverride(name="valorAliquota", column=@Column(name="VLR_ALIQ_COFINS", scale = 4, precision = 15)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_COFINS", scale = 2, precision = 15))
})
public class COFINS extends COFINSBase implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7091796097723990615L;
	

	
	/**
	 * Construtor padrão.
	 */
	public COFINS() {
		
	}

}
