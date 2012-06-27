package br.com.abril.nds.util.export.fiscal.nota.condition;

import java.util.List;

public class ConditionDefault implements Condition {

	@Override
	public boolean valid(Object object) {
		return true;
	}

	@Override
	public boolean validParent(Object object) {
		return true;
	}

	@Override
	public boolean validParents(List<Object> object) {
		return true;
	}

}
