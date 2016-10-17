package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@Embeddable
@AttributeOverrides({
		@AttributeOverride(name = "cst", column = @Column(name = "CST_ICMS_ST", length = 2, nullable = true)),
		@AttributeOverride(name = "valorBaseCalculo", column = @Column(name = "VLR_BASE_CALC_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "aliquota", column = @Column(name = "ALIQUOTA_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "valor", column = @Column(name = "VLR_ICMS_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "percentualReducao", column = @Column(name = "PERCENTUAL_REDUCAO_ST", precision = 5, scale = 2, nullable = true)),
		@AttributeOverride(name = "origem", column =@Column(name = "ORIGEM_ST", length = 1, nullable = true)),
		@AttributeOverride(name = "modelidade", column = @Column(name = "MODELIDADE_ST", length = 1, nullable = true))

})
public class ICMSST extends ICMSBase implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -540037483080106763L;

	
	/**
	 * pMVAST
	 */
	@Column(name="PERCENTUAL_ADCIONADO_ST", precision=18, scale=4, nullable=true)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS_ST_10, export = @NFEExport(secao = TipoSecao.N03, posicao = 7)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_30, export = @NFEExport(secao = TipoSecao.N05, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 8)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 8))
	})
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
	
	@XmlElement(name="orig")
	public String getAOrig() {
		return super.aOrig;
	}
	
	@XmlElement(name="CSOSN")
	public String getCst() {
		return super.getCst();
	}

}
