package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.IPI;

public class IPINTCondition extends IPICondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object) 
				&& (ConstantsCondition.IPI_CST_01.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_02.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_03.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_04.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_05.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_51.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_52.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_53.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_54.equals(((IPI)object).getCst())
						|| ConstantsCondition.IPI_CST_55.equals(((IPI)object).getCst()));
	}

}
