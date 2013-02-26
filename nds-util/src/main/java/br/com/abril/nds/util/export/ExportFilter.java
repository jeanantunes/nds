package br.com.abril.nds.util.export;


public class ExportFilter implements Comparable<ExportFilter> {

	private String label;
	
	private String value;
	
	private Export.Alignment alignment;
	
	private Integer exhibitionOrder;
	
	private Float widthPercent;
	
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

	/**
	 * @return the exhibitionOrder
	 */
	public int getExhibitionOrder() {
		return exhibitionOrder;
	}

	/**
	 * @param exhibitionOrder the exhibitionOrder to set
	 */
	public void setExhibitionOrder(int exhibitionOrder) {
		this.exhibitionOrder = exhibitionOrder;
	}

	@Override
	public int compareTo(ExportFilter exportFilter) {
		
		return this.exhibitionOrder.compareTo(exportFilter.exhibitionOrder);
	}

	public Float getWidthPercent() {
		return widthPercent;
	}

	public void setWidthPercent(Float widthPercent) {
		this.widthPercent = widthPercent;
	}

}
