package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ICMS;

public class ICMS00Condition extends ICMSCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ConstantsCondition.ICMS_CST_00.equals(((ICMS) object).getCst());
	}

}
