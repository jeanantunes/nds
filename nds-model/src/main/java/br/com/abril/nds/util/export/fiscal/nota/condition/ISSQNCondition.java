package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ISSQN;

public class ISSQNCondition implements Condition {

	@Override
	public boolean valid(Object object) {
		return true;
	}

	@Override
	public boolean validParent(Object object) {
		return (object instanceof ISSQN);
	}

}
