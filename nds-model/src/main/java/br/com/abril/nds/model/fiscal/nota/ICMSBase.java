package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class ICMSBase extends ImpostoProduto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8485591396388472998L;


	/**
	 * pRedBC
	 */
	@Column(name="PERCENTUAL_REDUCAO",precision=5, scale=2, nullable=true)
	protected BigDecimal percentualReducao;
	
	
	/**
	 * orig
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ORIGEM", length = 1, nullable = false)
	protected Origem origem;
	
	/**
	 * modBC
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MODELIDADE", length = 1, nullable = true)
	protected Modelidade modelidade;

	/**
	 * @return the percentualReducao
	 */
	public BigDecimal getPercentualReducao() {
		return percentualReducao;
	}

	/**
	 * @param percentualReducao the percentualReducao to set
	 */
	public void setPercentualReducao(BigDecimal percentualReducao) {
		this.percentualReducao = percentualReducao;
	}

	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * @return the modelidade
	 */
	public Modelidade getModelidade() {
		return modelidade;
	}

	/**
	 * @param modelidade the modelidade to set
	 */
	public void setModelidade(Modelidade modelidade) {
		this.modelidade = modelidade;
	}

}
