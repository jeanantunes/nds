package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;


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
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMSST10, export = @NFEExport(secao = TipoSecao.N03, posicao = 8)),
			@NFEWhen(condition = NFEConditions.ICMSST30, export = @NFEExport(secao = TipoSecao.N05, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMSST70, export = @NFEExport(secao = TipoSecao.N09, posicao = 9)),
			@NFEWhen(condition = NFEConditions.ICMSST90, export = @NFEExport(secao = TipoSecao.N10, posicao = 9))
	})
	protected BigDecimal percentualReducao;
	
	
	/**
	 * orig
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ORIGEM", length = 1, nullable = false)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS30, export = @NFEExport(secao = TipoSecao.N05, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS40, export = @NFEExport(secao = TipoSecao.N06, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS60, export = @NFEExport(secao = TipoSecao.N08, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 0, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 0, tamanho = 1))
	})
	protected Origem origem;
	
	/**
	 * modBC
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "MODELIDADE", length = 1, nullable = true)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMSST10, export = @NFEExport(secao = TipoSecao.N03, posicao = 6, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMSST30, export = @NFEExport(secao = TipoSecao.N05, posicao = 2, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMSST70, export = @NFEExport(secao = TipoSecao.N09, posicao = 7, tamanho = 1)),
			@NFEWhen(condition = NFEConditions.ICMSST90, export = @NFEExport(secao = TipoSecao.N10, posicao = 7, tamanho = 1))
	})
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
