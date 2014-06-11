package br.com.abril.nds.util.export.fiscal.nota.condition;

import java.util.List;

import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.ICMSST;

public class ICMSST60Condition extends ICMSSTCondition {

	@Override
	public boolean validParents(List<Object> object) {
		boolean isValid = false;
		for (Object obj: object) {
			if ((obj instanceof ICMS) && !(obj instanceof ICMSST)) {
				isValid = isValid || ConstantsCondition.ICMS_CST_00.equals(((ICMS) object).getCst());
			}
		}
		return isValid;
	}

}
