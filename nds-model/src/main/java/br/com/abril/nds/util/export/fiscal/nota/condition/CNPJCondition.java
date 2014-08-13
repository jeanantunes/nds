package br.com.abril.nds.util.export.fiscal.nota.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.stella.validation.CNPJValidator;

public class CNPJCondition extends ConditionDefault {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CNPJCondition.class);

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
