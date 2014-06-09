package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@XmlTransient
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
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS_20, export = @NFEExport(secao = TipoSecao.N04, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS_51, export = @NFEExport(secao = TipoSecao.N07, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_10, export = @NFEExport(secao = TipoSecao.N03, posicao = 8)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_30, export = @NFEExport(secao = TipoSecao.N05, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 9)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 9))
	})
	protected BigDecimal percentualReducao;
	
	
	/**
	 * orig
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ORIGEM", length = 1, nullable = false)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS_00, export = @NFEExport(secao = TipoSecao.N02, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_10, export = @NFEExport(secao = TipoSecao.N03, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_20, export = @NFEExport(secao = TipoSecao.N04, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_30, export = @NFEExport(secao = TipoSecao.N05, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_40, export = @NFEExport(secao = TipoSecao.N06, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_51, export = @NFEExport(secao = TipoSecao.N07, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_60, export = @NFEExport(secao = TipoSecao.N08, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 0, tamanho = 1))
	})
	protected OrigemProduto origem;
	
	@Transient
	protected String aOrig;
	
	/**
	 * modBC
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MODELIDADE", length = 1, nullable = true)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS_00, export = @NFEExport(secao = TipoSecao.N02, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_10, export = @NFEExport(secao = TipoSecao.N03, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_20, export = @NFEExport(secao = TipoSecao.N04, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_51, export = @NFEExport(secao = TipoSecao.N07, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_10, export = @NFEExport(secao = TipoSecao.N03, posicao = 6, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_30, export = @NFEExport(secao = TipoSecao.N05, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_70, export = @NFEExport(secao = TipoSecao.N09, posicao = 7, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS_ST_90, export = @NFEExport(secao = TipoSecao.N10, posicao = 7, tamanho = 1))
	})
	protected Modelidade modelidade;

	/**
	 * @return the percentualReducao
	 */
	@XmlTransient
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
	@XmlTransient
	public OrigemProduto getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(OrigemProduto origem) {
		this.origem = origem;
		this.aOrig = String.valueOf(origem.getId());
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
