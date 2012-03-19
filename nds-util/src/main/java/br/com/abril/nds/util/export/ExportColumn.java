package br.com.abril.nds.util.export;

public class ExportColumn {
	
	private String value;
	
	private Export.Alignment alignment;
	
	public ExportColumn() {
		
	}
	
	public ExportColumn(String value) {
	
		this.value = value;
	}
	
	public ExportColumn(String value, Export.Alignment alignment) {
		
		this.value = value;
		this.alignment = alignment;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the alignment
	 */
	public Export.Alignment getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(Export.Alignment alignment) {
		this.alignment = alignment;
	}
	
}
