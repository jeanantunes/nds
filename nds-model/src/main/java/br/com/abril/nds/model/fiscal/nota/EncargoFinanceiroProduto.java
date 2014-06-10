package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Entity
@Table(name = "NOTA_FISCAL_ENCARGO_FINANCEIRO_PRODUTO")
public class EncargoFinanceiroProduto extends EncargoFinanceiro {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6496916286204117106L;

	@Embedded
	@NFEExportType(secaoPadrao = {TipoSecao.M, TipoSecao.N})
	private ICMS icms;
	
	@Embedded
	@NFEExportType(secaoPadrao = TipoSecao.O)
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
