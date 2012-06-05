package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ENCARGO_FINANCEIRO_PRODUTO_NOTA_FISCAL")
@DiscriminatorValue(value = "P")
public class EncargoFinanceiroProduto extends EncargoFinanceiro {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6496916286204117106L;

	@Embedded
	private ICMS icms;
	
	@Embedded
	private IPI ipi;
	
	/**
	 * @return the icms
	 */
	public ICMS getIcms() {
		return icms;
	}

	/**
	 * @param icms the icms to set
	 */
	public void setIcms(ICMS icms) {
		this.icms = icms;
	}

	/**
	 * @return the ipi
	 */
	public IPI getIpi() {
		return ipi;
	}

	/**
	 * @param ipi the ipi to set
	 */
	public void setIpi(IPI ipi) {
		this.ipi = ipi;
	}

}
