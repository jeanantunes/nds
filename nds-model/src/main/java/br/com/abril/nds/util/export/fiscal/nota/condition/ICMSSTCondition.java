package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ICMSST;

public class ICMSSTCondition extends ConditionDefault {

	@Override
	public boolean validParent(Object object) {
		return (object instanceof ICMSST);
	}

}
