package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.ICMS;

public class ICMS40Condition extends ICMSCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& (ConstantsCondition.ICMS_CST_40.equals(((ICMS) object).getCst())
					|| ConstantsCondition.ICMS_CST_41.equals(((ICMS) object).getCst()) 
					|| ConstantsCondition.ICMS_CST_50.equals(((ICMS) object).getCst()));
	}

}
