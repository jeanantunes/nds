package br.com.abril.nds.util.export.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportFilterTestVO {

	@Export(label = "Filter One ", propertyToDynamicLabel = "dynamicProperty")
	private String filterOne;
	
	@Export(label = "Filter Two")
	private String filterTwo;
	
	@Export(label = "Filter Three")
	private String filterThree;
	
	@Export(label = "Filter Four")
	private String filterFour;
	
	@Export(label = "Filter Five", exhibitionOrder = 1)
	private String filterFive;
	
	@Export(label = "Filter Six")
	private String filterSix;
	
	@Export(label = "Filter Seven", exhibitionOrder = 2)
	private String filterSeven;
	
	private String dynamicProperty;

	public ExportFilterTestVO() {
		
	}
	
	public ExportFilterTestVO(String filterOne, String filterTwo,
							  String filterThree, String filterFour, String filterFive,
							  String filterSix, String filterSeven, String dynamicProperty) {
		
		this.filterOne = filterOne;
		this.filterTwo = filterTwo;
		this.filterThree = filterThree;
		this.filterFour = filterFour;
		this.filterFive = filterFive;
		this.filterSix = filterSix;
		this.filterSeven = filterSeven;
		this.dynamicProperty = dynamicProperty;
	}

	/**
	 * @return the filterOne
	 */
	public String getFilterOne() {
		return filterOne;
	}

	/**
	 * @param filterOne the filterOne to set
	 */
	public void setFilterOne(String filterOne) {
		this.filterOne = filterOne;
	}

	/**
	 * @return the filterTwo
	 */
	public String getFilterTwo() {
		return filterTwo;
	}

	/**
	 * @param filterTwo the filterTwo to set
	 */
	public void setFilterTwo(String filterTwo) {
		this.filterTwo = filterTwo;
	}

	/**
	 * @return the filterThree
	 */
	public String getFilterThree() {
		return filterThree;
	}

	/**
	 * @param filterThree the filterThree to set
	 */
	public void setFilterThree(String filterThree) {
		this.filterThree = filterThree;
	}

	/**
	 * @return the filterFour
	 */
	public String getFilterFour() {
		return filterFour;
	}

	/**
	 * @param filterFour the filterFour to set
	 */
	public void setFilterFour(String filterFour) {
		this.filterFour = filterFour;
	}

	/**
	 * @return the filterFive
	 */
	public String getFilterFive() {
		return filterFive;
	}

	/**
	 * @param filterFive the filterFive to set
	 */
	public void setFilterFive(String filterFive) {
		this.filterFive = filterFive;
	}

	/**
	 * @return the filterSix
	 */
	public String getFilterSix() {
		return filterSix;
	}

	/**
	 * @param filterSix the filterSix to set
	 */
	public void setFilterSix(String filterSix) {
		this.filterSix = filterSix;
	}

	/**
	 * @return the filterSeven
	 */
	public String getFilterSeven() {
		return filterSeven;
	}

	/**
	 * @param filterSeven the filterSeven to set
	 */
	public void setFilterSeven(String filterSeven) {
		this.filterSeven = filterSeven;
	}

	/**
	 * @return the dynamicProperty
	 */
	public String getDynamicProperty() {
		return dynamicProperty;
	}

	/**
	 * @param dynamicProperty the dynamicProperty to set
	 */
	public void setDynamicProperty(String dynamicProperty) {
		this.dynamicProperty = dynamicProperty;
	}
	
}
