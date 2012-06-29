package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@MappedSuperclass
public abstract class Imposto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 536918061402194564L;

	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS30, export = @NFEExport(secao = TipoSecao.N05, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS40, export = @NFEExport(secao = TipoSecao.N06, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS60, export = @NFEExport(secao = TipoSecao.N08, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 1, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 1, tamanho = 2))
	})
	protected String cst;
	
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 6)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 6)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 6)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 6)),
			@NFEWhen(condition = NFEConditions.ICMSST10, export = @NFEExport(secao = TipoSecao.N03, posicao = 11)),
			@NFEWhen(condition = NFEConditions.ICMSST30, export = @NFEExport(secao = TipoSecao.N05, posicao = 7)),
			@NFEWhen(condition = NFEConditions.ICMSST60, export = @NFEExport(secao = TipoSecao.N08, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMSST70, export = @NFEExport(secao = TipoSecao.N09, posicao = 12)),
			@NFEWhen(condition = NFEConditions.ICMSST90, export = @NFEExport(secao = TipoSecao.N10, posicao = 12))
	})
	protected BigDecimal valor;
	
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 3)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMSST10, export = @NFEExport(secao = TipoSecao.N03, posicao = 9)),
			@NFEWhen(condition = NFEConditions.ICMSST30, export = @NFEExport(secao = TipoSecao.N05, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMSST60, export = @NFEExport(secao = TipoSecao.N08, posicao = 2)),
			@NFEWhen(condition = NFEConditions.ICMSST70, export = @NFEExport(secao = TipoSecao.N09, posicao = 10)),
			@NFEWhen(condition = NFEConditions.ICMSST90, export = @NFEExport(secao = TipoSecao.N10, posicao = 10))
	})
	protected BigDecimal valorBaseCalculo;
	
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.ICMS00, export = @NFEExport(secao = TipoSecao.N02, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS10, export = @NFEExport(secao = TipoSecao.N03, posicao = 4)),
			@NFEWhen(condition = NFEConditions.ICMS20, export = @NFEExport(secao = TipoSecao.N04, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMS51, export = @NFEExport(secao = TipoSecao.N07, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMS70, export = @NFEExport(secao = TipoSecao.N09, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMS90, export = @NFEExport(secao = TipoSecao.N10, posicao = 5)),
			@NFEWhen(condition = NFEConditions.ICMSST10, export = @NFEExport(secao = TipoSecao.N03, posicao = 10)),
			@NFEWhen(condition = NFEConditions.ICMSST30, export = @NFEExport(secao = TipoSecao.N05, posicao = 6)),
			@NFEWhen(condition = NFEConditions.ICMSST70, export = @NFEExport(secao = TipoSecao.N09, posicao = 11)),
			@NFEWhen(condition = NFEConditions.ICMSST90, export = @NFEExport(secao = TipoSecao.N10, posicao = 11))
	})
	protected BigDecimal aliquota;

	/**
	 * @return the cst
	 */
	public String getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(String cst) {
		this.cst = cst;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the valorBaseCalculo
	 */
	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	/**
	 * @return the aliquota
	 */
	public BigDecimal getAliquota() {
		return aliquota;
	}

	/**
	 * @param aliquota the aliquota to set
	 */
	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}
	
}
