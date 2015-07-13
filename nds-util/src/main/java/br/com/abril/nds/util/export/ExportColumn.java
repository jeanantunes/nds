package br.com.abril.nds.util.export;

public class ExportColumn implements Comparable<ExportColumn> {
	
	private String value;
	
	private Export.Alignment alignment;
	
	private Integer exhibitionOrder;
	
	private ColumnType columnType;
	
	private Float fontSize;
	
	public ExportColumn() {
		
	}
	
	public ExportColumn(String value) {
	
		this.value = value;
	}
	
	public ExportColumn(String value, Export.Alignment alignment, Integer exhibitionOrder, ColumnType columnType,
			Float fontSize) {
		
		this.value = value;
		this.alignment = alignment;
		this.exhibitionOrder = exhibitionOrder;
		this.columnType = columnType;
		this.fontSize = fontSize;
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

	@Override
	public int compareTo(ExportColumn exportColumn) {
		
		return this.exhibitionOrder.compareTo(exportColumn.exhibitionOrder);
	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}

	public Float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}
	
}
