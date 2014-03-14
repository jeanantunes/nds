package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_PIS", length = 2)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_PIS",  scale = 2,precision = 15)),
    @AttributeOverride(name="percentualAliquota", column=@Column(name="PER_ALIQ_PIS", scale = 2, precision = 5)),
    @AttributeOverride(name="quantidadeVendida", column=@Column(name="QTD_VENDIDA_PIS", scale = 4,  precision= 16)),
    @AttributeOverride(name="valorAliquota", column=@Column(name="VLR_ALIQ_PIS", scale = 4, precision = 15)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_PIS", scale = 2, precision = 15))
})
public class PIS extends PISBase implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8687019293575282492L;
	
	/**
	 * Construtor padrão.
	 */
	public PIS() {
		
	}
	
	/**
	 * @return the cst
	 */
	@XmlElement(name="CST")
	public String getCst() {
		return super.getCst();
	}

	/**
	 * @return the valorBaseCalculo
	 */
	@XmlElement(name="vBC")
	public BigDecimal getDValorBaseCalculo() {
		return super.getValorBaseCalculo();
	}

	/**
	 * @return the percentualAliquota
	 */
	@XmlElement(name="pPIS")
	public BigDecimal getPercentualAliquota() {
		return super.getPercentualAliquota();
	}
	
	/**
	 * @return the valor
	 */
	@XmlElement(name="vPIS")
	public BigDecimal getValor() {
		return super.getValor();
	}
}
