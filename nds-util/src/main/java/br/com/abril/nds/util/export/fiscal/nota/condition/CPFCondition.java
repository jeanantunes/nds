package br.com.abril.nds.util.export.fiscal.nota.condition;


public class CPFCondition implements Condition {

	@Override
	public boolean valid(Object object) {
		return false;
	}

	@Override
	public boolean validParent(Object object) {		
		return true;
	}

}
