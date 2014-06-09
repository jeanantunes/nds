package br.com.abril.nds.model.cadastro;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AssociacaoTipoProduto {
	
	@ManyToOne(optional = false, cascade=CascadeType.MERGE)
	@JoinColumn(name = "TIPO_PRODUTO_ID")
	private TipoProduto tipoProduto;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;

	/**
	 * @return the tipoProduto
	 */
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	/**
	 * @param tipoProduto the tipoProduto to set
	 */
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	/**
	 * @return the principal
	 */
	public boolean isPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

}
