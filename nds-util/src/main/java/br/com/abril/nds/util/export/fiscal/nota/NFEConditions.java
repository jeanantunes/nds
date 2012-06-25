package br.com.abril.nds.util.export.fiscal.nota;

import br.com.abril.nds.util.export.fiscal.nota.condition.CNPJCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.CPFCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.Condition;

public enum NFEConditions {	
	
	CPF(new CPFCondition()), CNPJ(new CNPJCondition());
	
	
	
	private Condition condition;

	private NFEConditions(Condition condition) {
		this.condition = condition;
	}

	/**
	 * @param object
	 * @return
	 * @see br.com.abril.nds.util.export.fiscal.nota.condition.Condition#valid(java.lang.Object)
	 */
	public boolean valid(Object object) {
		return condition.valid(object);
	}
	
	
}