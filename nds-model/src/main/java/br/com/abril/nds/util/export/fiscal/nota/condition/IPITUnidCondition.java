package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.IPI;

public class IPITUnidCondition extends IPICondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ((IPI)object).getQuantidadeUnidades() != null;
	}

}
