package br.com.abril.nds.dto;

public class ComponenteElementoDTO {

	private String tipo;
	private String id;
	private String value;

	public ComponenteElementoDTO(String tipo, Object id, String value) {
		this.tipo = tipo;
		this.value = value;
		this.id = id.toString();
	}

	public String getId() {
		return id==null?"":id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo==null?"":tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValue() {
		return value==null?"":value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
