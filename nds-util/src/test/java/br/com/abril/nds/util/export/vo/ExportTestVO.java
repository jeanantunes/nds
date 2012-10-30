package br.com.abril.nds.util.export.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ExportTestVO {

	@Export(label = "Column1 ", alignment = Alignment.CENTER, exhibitionOrder = 1, propertyToDynamicLabel = "dynamicProperty")
	private String Column1;
	
	@Export(label = "Column2", alignment = Alignment.RIGHT, exhibitionOrder = 2)
	private String Column2;
	
	@Export(label = "Column3", alignment = Alignment.LEFT)
	private String Column3;
	
	@Export(label = "Column4", alignment = Alignment.CENTER)
	private String Column4;
	
	@Export(label = "Column5", alignment = Alignment.RIGHT)
	private String Column5;
	
	@Export(label = "Column6", alignment = Alignment.CENTER)
	private String Column6;
	
	@Export(label = "Column7", alignment = Alignment.LEFT)
	private String Column7;
	
	@Export(label = "Column8", alignment = Alignment.RIGHT)
	private String Column8;
	
	@Export(label = "Column9", alignment = Alignment.RIGHT)
	private String Column9;
	
	@Export(label = "Column10", alignment = Alignment.CENTER)
	private String Column10;
	
	private String dynamicProperty;

	public ExportTestVO(String column1, String column2, String column3,
						String column4, String column5, String column6, String column7,
						String column8, String column9, String column10, String dynamicProperty) {
		
		this.Column1 = column1;
		this.Column2 = column2;
		this.Column3 = column3;
		this.Column4 = column4;
		this.Column5 = column5;
		this.Column6 = column6;
		this.Column7 = column7;
		this.Column8 = column8;
		this.Column9 = column9;
		this.Column10 = column10;
		this.dynamicProperty = dynamicProperty;
	}

	public String getColumn1() {
		return Column1;
	}

	public void setColumn1(String column1) {
		Column1 = column1;
	}

	public String getColumn2() {
		return Column2;
	}

	public void setColumn2(String column2) {
		Column2 = column2;
	}

	public String getColumn3() {
		return Column3;
	}

	public void setColumn3(String column3) {
		Column3 = column3;
	}

	public String getColumn4() {
		return Column4;
	}

	public void setColumn4(String column4) {
		Column4 = column4;
	}

	public String getColumn5() {
		return Column5;
	}

	public void setColumn5(String column5) {
		Column5 = column5;
	}

	public String getColumn6() {
		return Column6;
	}

	public void setColumn6(String column6) {
		Column6 = column6;
	}

	public String getColumn7() {
		return Column7;
	}

	public void setColumn7(String column7) {
		Column7 = column7;
	}

	public String getColumn8() {
		return Column8;
	}

	public void setColumn8(String column8) {
		Column8 = column8;
	}

	public String getColumn9() {
		return Column9;
	}

	public void setColumn9(String column9) {
		Column9 = column9;
	}

	public String getColumn10() {
		return Column10;
	}

	public void setColumn10(String column10) {
		Column10 = column10;
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
