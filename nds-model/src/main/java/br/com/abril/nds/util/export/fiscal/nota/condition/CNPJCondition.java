package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.caelum.stella.validation.CNPJValidator;

public class CNPJCondition extends ConditionDefault {

	@Override
	public boolean valid(Object object) {	
		try {
			new CNPJValidator().assertValid(object.toString());
			return true;
		} catch (Exception exception) {
			try {
				new CNPJValidator(false).assertValid(object.toString());
				return true;
			} catch (Exception subException) {
				subException.printStackTrace();
				return false;
			}
		}
	}

}
