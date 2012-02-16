package br.com.abril.nds.util;

public class ItemAutoComplete {

	public ItemAutoComplete(String value, Object chave){
		this.value = value;
		this.chave = chave;
	}
	
	private String value;
	
	private Object chave;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getChave() {
		return chave;
	}

	public void setChave(Object chave) {
		this.chave = chave;
	}
}
