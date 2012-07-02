package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.PIS;

public class PISNTCondition extends PISCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& (ConstantsCondition.PIS_CST_04.equals(((PIS)object).getCst())
						|| ConstantsCondition.PIS_CST_06.equals(((PIS)object).getCst())
						|| ConstantsCondition.PIS_CST_07.equals(((PIS)object).getCst())
						|| ConstantsCondition.PIS_CST_08.equals(((PIS)object).getCst())
						|| ConstantsCondition.PIS_CST_09.equals(((PIS)object).getCst()));
	}

}
