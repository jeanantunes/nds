package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.PIS;

public class PISTAliqCondition extends PISCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& (ConstantsCondition.PIS_CST_01.equals(((PIS)object).getCst())
						|| ConstantsCondition.PIS_CST_02.equals(((PIS)object).getCst()));
	}

}
