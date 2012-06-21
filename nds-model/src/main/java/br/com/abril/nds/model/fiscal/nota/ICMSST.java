package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
		@AttributeOverride(name = "cst", column = @Column(name = "CST_ICMS_ST", length = 2, nullable = true)),
		@AttributeOverride(name = "valorBaseCalculo", column = @Column(name = "VLR_BASE_CALC_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "aliquota", column = @Column(name = "ALIQUOTA_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "valor", column = @Column(name = "VLR_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "percentualReducao", column = @Column(name = "PERCENTUAL_REDUCAO_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "origem", column =@Column(name = "ORIGEM_ST", length = 1, nullable = true)),
		@AttributeOverride(name = "modelidade", column = @Column(name = "MODELIDADE_ST", length = 1, nullable = true)),

})
public class ICMSST extends ICMSBase implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -540037483080106763L;

	
	/**
	 * pMVAST
	 */
	@Column(name="PERCENTUAL_ADCIONADO_ST", precision=5,scale=2, nullable=true)
	private BigDecimal percentualAdicionado;
	
	/**
	 * Construtor padrão.
	 */
	public ICMSST() {

	}

	/**
	 * @return the percentualAdicionado
	 */
	public BigDecimal getPercentualAdicionado() {
		return percentualAdicionado;
	}

	/**
	 * @param percentualAdicionado the percentualAdicionado to set
	 */
	public void setPercentualAdicionado(BigDecimal percentualAdicionado) {
		this.percentualAdicionado = percentualAdicionado;
	}

}
