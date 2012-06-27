package br.com.abril.nds.util.export.fiscal.nota.condition;

public interface Condition {
	
	public abstract boolean valid(Object object);

	public abstract boolean validParent(Object object);

}
