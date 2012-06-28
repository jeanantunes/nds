package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.caelum.stella.validation.CPFValidator;


public class CPFCondition extends ConditionDefault {

	@Override
	public boolean valid(Object object) {
		try {
			new CPFValidator().assertValid(object.toString());
			return true;
		} catch (Exception exception) {
			try {
				new CPFValidator(false).assertValid(object.toString());
				return true;
			} catch (Exception subException) {
				subException.printStackTrace();
				return false;
			}
		}
	}

}
