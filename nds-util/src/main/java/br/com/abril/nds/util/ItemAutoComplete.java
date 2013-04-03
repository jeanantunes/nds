package br.com.abril.nds.util;

public class ItemAutoComplete {

	public ItemAutoComplete() {
	}
	public ItemAutoComplete(String value, String label, Object chave){
		this.value = value;
		this.label = label;
		this.chave = chave;
	}
	
	private String value;
	
	private String label;
	
	private Object chave;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getChave() {
		return chave;
	}

	public void setChave(Object chave) {
		this.chave = chave;
	}
}