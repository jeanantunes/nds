package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.PIS;

public class PISTQtdeCondition extends PISCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ConstantsCondition.PIS_CST_03.equals(((PIS)object).getCst());
	}

}
