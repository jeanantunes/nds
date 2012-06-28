package br.com.abril.nds.util.export.fiscal.nota.condition;

import java.util.List;

public class IdentificacaoDestinatario extends ConditionDefault {

	@Override
	public boolean validParents(List<Object> object){
		boolean isValid = false;
		for (Object obj: object) {
			isValid = isValid || obj instanceof IdentificacaoDestinatario;
		}
		return isValid;
	}
	
}
