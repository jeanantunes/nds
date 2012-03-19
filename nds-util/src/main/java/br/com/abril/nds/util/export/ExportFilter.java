package br.com.abril.nds.util.export;


public class ExportFilter {

	private String label;
	
	private String value;
	
	private Export.Alignment alignment;
	
	/**
	 * Construtor padrão.
	 */
	public ExportFilter() {
		
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
