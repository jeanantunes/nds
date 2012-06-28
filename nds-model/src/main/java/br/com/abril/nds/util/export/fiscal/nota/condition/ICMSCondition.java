package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.ICMSST;

public class ICMSCondition extends ConditionDefault {

	@Override
	public boolean validParent(Object object) {
		return (object instanceof ICMS) && !(object instanceof ICMSST);
	}

}
