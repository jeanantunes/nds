package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;


public class CPFCondition extends ConditionDefault {

	@Override
	public boolean valid(Object object) {
		
		try {
			new CPFValidator().assertValid(object.toString());
		} catch (InvalidStateException e) {
			
			StringBuilder msgs = new StringBuilder();
			
			for (ValidationMessage m : e.getInvalidMessages()){
				
				if (msgs.length() != 0){
					
					msgs.append(", ");
				}
				
				msgs.append(m.getMessage()).append(" ").append(object);
			}
			
			throw new ValidacaoException(TipoMensagem.ERROR, msgs.toString());
		}
		return true;
	}

}
