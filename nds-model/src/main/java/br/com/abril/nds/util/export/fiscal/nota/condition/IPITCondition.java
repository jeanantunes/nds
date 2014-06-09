package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.IPI;

public class IPITCondition extends IPICondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object) 
				&& (ConstantsCondition.IPI_CST_00.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_49.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_50.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_99.equals(((IPI)object).getCst()));
	}

}
