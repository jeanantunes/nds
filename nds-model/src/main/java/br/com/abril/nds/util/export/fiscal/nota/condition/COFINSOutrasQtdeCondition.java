package br.com.abril.nds.util.export.fiscal.nota.condition;

import br.com.abril.nds.model.fiscal.nota.COFINS;

public class COFINSOutrasQtdeCondition extends COFINSOutrasCondition {

	@Override
	public boolean validParent(Object object) {
		return super.validParent(object)
				&& ((COFINS)object).getQuantidadeVendida() != null;
	}

}
