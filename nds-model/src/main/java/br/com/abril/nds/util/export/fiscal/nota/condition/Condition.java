package br.com.abril.nds.util.export.fiscal.nota.condition;

import java.util.List;

public interface Condition {
	
	public abstract boolean valid(Object object);

	public abstract boolean validParent(Object object);

	public abstract boolean validParents(List<Object> object);

}
