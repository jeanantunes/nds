package br.com.abril.nds.util.export;

/**
 * Representa a coluna do rodapé, de acordo com a configuração realizada no Bean a ser exportado.
 * 
 * @author Discover Technology
 *
 */
public class ExportFooterColumn extends ExportColumn {

	private String name;
	
	private String headerToAlign;
	
	private String label;
	
	private boolean verticalPrinting;
	
	private FooterType footerType;

	public ExportFooterColumn() { }
	
	public ExportFooterColumn(String value, Export.Alignment alignment, 
							  Integer exhibitionOrder, ColumnType columnType, Float fontSize) {
		super(value, alignment, exhibitionOrder, columnType, fontSize);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the verticalPrinting
	 */
	public boolean isVerticalPrinting() {
		return verticalPrinting;
	}

	/**
	 * @param verticalPrinting the verticalPrinting to set
	 */
	public void setVerticalPrinting(boolean verticalPrinting) {
		this.verticalPrinting = verticalPrinting;
	}

	/**
	 * @return the footerType
	 */
	public FooterType getFooterType() {
		return footerType;
	}

	/**
	 * @param footerType the footerType to set
	 */
	public void setFooterType(FooterType footerType) {
		this.footerType = footerType;
	}
}
