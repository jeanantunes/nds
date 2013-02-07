package br.com.abril.nds.integracao.engine.data;

public class FixedLengthField {
	private int offset;
	private int length;
	private String value;
	
	public FixedLengthField() {
	}
	
	public FixedLengthField(int offset, int length, String value) {
		this.offset = offset;
		this.length = length;
		this.value = value;
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
