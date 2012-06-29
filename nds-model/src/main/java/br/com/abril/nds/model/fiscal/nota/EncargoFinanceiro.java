package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "NOTA_FISCAL_ENCARGO_FINANCEIRO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class EncargoFinanceiro implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3648094277369869546L;

	@Id
	@GeneratedValue(generator = "NOTA_FISCAL_ENCARGO_FINANCEIRO_SEQ", strategy = GenerationType.TABLE)
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "PRODUTO_SERVICO_ID")
	private ProdutoServico produtoServico;
	
	@Embedded
	private PIS pis;	

	@Embedded
	private PISST pisSt;
	
	@Embedded
	private COFINS cofins;
	
	@Embedded
	private COFINSST cofinsSt;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the pis
	 */
	public PIS getPis() {
		return pis;
	}

	/**
	 * @param pis the pis to set
	 */
	public void setPis(PIS pis) {
		this.pis = pis;
	}

	/**
	 * @return the cofins
	 */
	public COFINS getCofins() {
		return cofins;
	}

	/**
	 * @param cofins the cofins to set
	 */
	public void setCofins(COFINS cofins) {
		this.cofins = cofins;
	}
	
	/**
	 * @return the produtoServico
	 */
	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	/**
	 * @param produtoServico the produtoServico to set
	 */
	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncargoFinanceiro other = (EncargoFinanceiro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the pisSt
	 */
	public PISST getPisSt() {
		return pisSt;
	}

	/**
	 * @param pisSt the pisSt to set
	 */
	public void setPisSt(PISST pisSt) {
		this.pisSt = pisSt;
	}

	/**
	 * @return the cofinsSt
	 */
	public COFINSST getCofinsSt() {
		return cofinsSt;
	}

	/**
	 * @param cofinsSt the cofinsSt to set
	 */
	public void setCofinsSt(COFINSST cofinsSt) {
		this.cofinsSt = cofinsSt;
	}

}
