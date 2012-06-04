package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ENCARGO_FINANCEIRO_SERVICO_NOTA_FISCAL")
@DiscriminatorValue(value = "S")
public class EncargoFinanceiroServico extends EncargoFinanceiro {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4813434383036209775L;
	
	@Embedded
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
