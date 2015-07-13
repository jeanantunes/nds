package br.com.abril.nds.util.export;

public class ExportHeader implements Comparable<ExportHeader> {
	
	private String value;
	
	private Export.Alignment alignment;
	
	private Integer exhibitionOrder;
	
	private Float widthPercent;
	
	private ColumnType columType;
	
	private Boolean xlsAutoSize;
	
	public ExportHeader() {
		
	}

	public ExportHeader(String value) {
		
		this.value = value;
	}
	
	public ExportHeader(String value, Export.Alignment alignment) {
		
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
	
	/**
	 * @return the exhibitionOrder
	 */
	public Integer getExhibitionOrder() {
		return exhibitionOrder;
	}

	/**
	 * @param exhibitionOrder the exhibitionOrder to set
	 */
	public void setExhibitionOrder(Integer exhibitionOrder) {
		this.exhibitionOrder = exhibitionOrder;
	}

	/**
	 * @return the columnWidth
	 */
	public Float getWidthPercent() {
		return widthPercent;
	}

	/**
	 * @param widthPercent the columnWidth to set
	 */
	public void setColumnWidth(Float widthPercent) {
		this.widthPercent = widthPercent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExportHeader other = (ExportHeader) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(ExportHeader exportHeader) {
		
		return this.exhibitionOrder.compareTo(exportHeader.exhibitionOrder);
	}

	public ColumnType getColumType() {
		return columType;
	}

	public void setColumType(ColumnType columType) {
		this.columType = columType;
	}

	public Boolean getXlsAutoSize() {
		return xlsAutoSize;
	}

	public void setXlsAutoSize(Boolean xlsAutoSize) {
		this.xlsAutoSize = xlsAutoSize;
	}

}
