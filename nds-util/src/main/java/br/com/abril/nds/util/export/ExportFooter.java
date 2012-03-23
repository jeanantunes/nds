package br.com.abril.nds.util.export;

public class ExportFooter {
	
	private String label;
	
	private String value;
	
	private Export.Alignment alignment;
	
	private String headerToAlign;

	/**
	 * Construtor padrão.
	 */
	public ExportFooter() {
		
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

	/**
	 * @return the headerToAlign
	 */
	public String getHeaderToAlign() {
		return headerToAlign;
	}

	/**
	 * @param headerToAlign the headerToAlign to set
	 */
	public void setHeaderToAlign(String headerToAlign) {
		this.headerToAlign = headerToAlign;
	}

}
