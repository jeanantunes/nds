package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.COFINS;

public class COFINSNTCondition extends COFINSCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& (ConstantsCondition.COFINS_CST_04.equals(((COFINS)object).getCst())
						|| ConstantsCondition.COFINS_CST_06.equals(((COFINS)object).getCst())
						|| ConstantsCondition.COFINS_CST_07.equals(((COFINS)object).getCst())
						|| ConstantsCondition.COFINS_CST_08.equals(((COFINS)object).getCst())
						|| ConstantsCondition.COFINS_CST_09.equals(((COFINS)object).getCst()));
	}

}
