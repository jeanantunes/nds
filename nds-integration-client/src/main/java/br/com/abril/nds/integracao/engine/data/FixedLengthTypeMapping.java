package br.com.abril.nds.integracao.engine.data;

public class FixedLengthTypeMapping<T> {
	private FixedLengthField field;
	private Class<T> targetClass;
	
	public FixedLengthTypeMapping(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
	
	public FixedLengthTypeMapping(FixedLengthField field, Class<T> targetClass) {
		this.field = field;
		this.targetClass = targetClass;
	}
	
	public FixedLengthField getField() {
		return field;
	}
	public void setField(FixedLengthField field) {
		this.field = field;
	}
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
}