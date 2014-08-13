package br.com.abril.nds.util.export.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportTestSmallVO {

	@Export(label = "Column1", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String Column1;
	
	@Export(label = "Column2", alignment = Alignment.RIGHT, exhibitionOrder = 2)
	private String Column2;
	
	@Export(label = "Column3", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String Column3;
	
	@Export(label = "Column4", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String Column4;
	
	@Export(label = "Column5", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String Column5;
	
	public ExportTestSmallVO(String column1, String column2, String column3,
							 String column4, String column5) {
	
		Column1 = column1;
		Column2 = column2;
		Column3 = column3;
		Column4 = column4;
		Column5 = column5;
	}

	/**
	 * @return the column1
	 */
	public String getColumn1() {
		return Column1;
	}

	/**
	 * @param column1 the column1 to set
	 */
	public void setColumn1(String column1) {
		Column1 = column1;
	}

	/**
	 * @return the column2
	 */
	public String getColumn2() {
		return Column2;
	}

	/**
	 * @param column2 the column2 to set
	 */
	public void setColumn2(String column2) {
		Column2 = column2;
	}

	/**
	 * @return the column3
	 */
	public String getColumn3() {
		return Column3;
	}

	/**
	 * @param column3 the column3 to set
	 */
	public void setColumn3(String column3) {
		Column3 = column3;
	}

	/**
	 * @return the column4
	 */
	public String getColumn4() {
		return Column4;
	}

	/**
	 * @param column4 the column4 to set
	 */
	public void setColumn4(String column4) {
		Column4 = column4;
	}

	/**
	 * @return the column5
	 */
	public String getColumn5() {
		return Column5;
	}

	/**
	 * @param column5 the column5 to set
	 */
	public void setColumn5(String column5) {
		Column5 = column5;
	}
	
}
