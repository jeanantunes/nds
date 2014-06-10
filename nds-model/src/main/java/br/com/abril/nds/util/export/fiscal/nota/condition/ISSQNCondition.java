package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ISSQN;

public class ISSQNCondition extends ConditionDefault {


	@Override
	public boolean validParent(Object object) {
		return (object instanceof ISSQN);
	}

}
