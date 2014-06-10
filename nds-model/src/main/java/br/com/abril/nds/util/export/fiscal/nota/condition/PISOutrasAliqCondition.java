package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.PIS;

public class PISOutrasAliqCondition extends PISOutrasCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ((PIS)object).getPercentualAliquota() != null;
	}

}
