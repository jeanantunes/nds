package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.caelum.stella.validation.CPFValidator;


public class CPFCondition implements Condition {

	@Override
	public boolean valid(Object object) {
		try {
			new CPFValidator().assertValid(object.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean validParent(Object object) {		
		return true;
	}

}
