package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.COFINS;

public class COFINSTQtdeCondition extends COFINSCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ConstantsCondition.COFINS_CST_03.equals(((COFINS)object).getCst());
	}

}
