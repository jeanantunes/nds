package br.com.abril.nds.integracao.engine.data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class FixedLengthRouteTemplate extends FileRouteTemplate {
	private List<FixedLengthTypeMapping> typeMappings = new ArrayList<FixedLengthTypeMapping>();
	
	private FixedLengthTypeMapping typeMapping;
	
	public <T> void setTypeMapping(Class<T> targetClass) {
		this.typeMapping = new FixedLengthTypeMapping<T>(targetClass);
	}
	
	public <T> FixedLengthTypeMapping addTypeMapping(FixedLengthField field, Class<T> targetClass) {
		FixedLengthTypeMapping typeMapping = new FixedLengthTypeMapping<T>(field, targetClass);
		
		typeMappings.add(typeMapping);
		
		return typeMapping;
	}
	
	public List<FixedLengthTypeMapping> getTypeMappings() {
		return typeMappings;
	}
	
	public void setTypeMappings(List<FixedLengthTypeMapping> typeMappings) {
		this.typeMappings = typeMappings;
	}

	public FixedLengthTypeMapping getTypeMapping() {
		return typeMapping;
	}

	public void setTypeMapping(FixedLengthTypeMapping typeMapping) {
		this.typeMapping = typeMapping;
	}
}
