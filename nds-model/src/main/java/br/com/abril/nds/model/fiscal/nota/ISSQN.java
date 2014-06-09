package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_ISSQN", length = 1, nullable = false)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_ISSQN", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_ISSQN", precision = 5, scale = 2, nullable = false)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_ISSQN", precision = 5, scale = 2, nullable = false))
})
public class ISSQN extends ImpostoServico implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1762681520302597480L;	
	
	@Column(name = "COD_MUNICIPIO", length = 7, nullable = false)
	@NFEExport(secao = TipoSecao.U, posicao = 3)
	private Integer codigoMunicipio;	
	
	@Column(name = "ITEM_LISTA_SERVICO", length = 4, nullable = false)
	@NFEExport(secao = TipoSecao.U, posicao = 4)
	private Integer itemListaServico;
	
	/**
	 * Construtor padrão.
	 */
	public ISSQN() {
		
	}

	/**
	 * @return the codigoMunicipio
	 */
	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	/**
	 * @param codigoMunicipio the codigoMunicipio to set
	 */
	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	/**
	 * @return the itemListaServico
	 */
	public Integer getItemListaServico() {
		return itemListaServico;
	}

	/**
	 * @param itemListaServico the itemListaServico to set
	 */
	public void setItemListaServico(Integer itemListaServico) {
		this.itemListaServico = itemListaServico;
	}
	
}
