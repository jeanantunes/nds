package br.com.abril.nds.util.export.fiscal.nota;

import java.util.List;

import br.com.abril.nds.util.export.fiscal.nota.condition.CNPJCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.CPFCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IdentificacaoDestinatarioCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IdentificacaoEmitenteCondition;

public enum NFEConditions {	
	
	CPF(new CPFCondition()), 
	CNPJ(new CNPJCondition()),
	IDENTIFICACAO_EMITENTE(new IdentificacaoEmitenteCondition()),
	IDENTIFICACAO_DESTINATARIO(new IdentificacaoDestinatarioCondition());
	
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
	
	public boolean validParent(Object object) {
		return condition.validParent(object);
	}
	
	public boolean validParents(List<Object> object) {
		return condition.validParents(object);
	}
	
}