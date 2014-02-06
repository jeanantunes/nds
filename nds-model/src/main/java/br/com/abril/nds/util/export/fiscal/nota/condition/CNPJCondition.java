package br.com.abril.nds.util.export.fiscal.nota.condition;

import org.apache.log4j.Logger;

import br.com.caelum.stella.validation.CNPJValidator;

public class CNPJCondition extends ConditionDefault {
    
    private static final Logger LOGGER = Logger.getLogger(CNPJCondition.class);

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
                LOGGER.error(subException.getMessage(), subException);
				return false;
			}
		}
	}

}
