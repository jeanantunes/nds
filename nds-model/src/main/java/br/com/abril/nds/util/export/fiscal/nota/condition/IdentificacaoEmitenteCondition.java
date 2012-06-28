package br.com.abril.nds.util.export.fiscal.nota.condition;

import java.util.List;

import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;

public class IdentificacaoEmitenteCondition extends ConditionDefault {
	
	@Override
	public boolean validParents(List<Object> object){
		boolean isValid = false;
		for (Object obj: object) {
			isValid = isValid || obj instanceof IdentificacaoEmitente;
		}
		return isValid;
	}
	
}
