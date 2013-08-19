package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Entity
@Table(name = "NOTA_FISCAL_ENCARGO_FINANCEIRO_SERVICO")
public class EncargoFinanceiroServico extends EncargoFinanceiro {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4813434383036209775L;
	
	@Embedded
	@NFEExportType
	private ISSQN issqn;

	/**
	 * @return the issqn
	 */
	public ISSQN getIssqn() {
		return issqn;
	}

	/**
	 * @param issqn the issqn to set
	 */
	public void setIssqn(ISSQN issqn) {
		this.issqn = issqn;
	}

}
