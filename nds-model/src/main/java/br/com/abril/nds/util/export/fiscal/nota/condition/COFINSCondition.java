package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.COFINS;

public class COFINSCondition extends ConditionDefault {

	@Override
	public boolean validParent(Object object) {
		return (object instanceof COFINS);
	}

}
