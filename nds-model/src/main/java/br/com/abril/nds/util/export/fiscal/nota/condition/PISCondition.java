package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.PIS;
import br.com.abril.nds.model.fiscal.nota.PISST;

public class PISCondition extends ConditionDefault {

	@Override
	public boolean validParent(Object object) {
		return (object instanceof PIS) && !(object instanceof PISST);
	}

}
