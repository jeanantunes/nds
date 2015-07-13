package br.com.abril.nds.util.export.vo;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportTestVO {

	@Export(label = "Column1 ", alignment = Alignment.CENTER, widthPercent = 20, exhibitionOrder = 1, propertyToDynamicLabel = "dynamicProperty")
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

    @Export(label = "Column7", alignment = Alignment.LEFT, columnType = ColumnType.INTEGER)
	private String Column7;

    @Export(label = "Column8", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String Column8;

    @Export(label = "Column9", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA)
	private String Column9;

    @Export(label = "Column10", alignment = Alignment.CENTER, columnType = ColumnType.DECIMAL)
	private String Column10;

	private String dynamicProperty;

	public ExportTestVO(final String column1, final String column2, final String column3,
						final String column4, final String column5, final String column6, final String column7,
						final String column8, final String column9, final String column10, final String dynamicProperty) {

		Column1 = column1;
		Column2 = column2;
		Column3 = column3;
		Column4 = column4;
		Column5 = column5;
		Column6 = column6;
		Column7 = column7;
		Column8 = column8;
		Column9 = column9;
		Column10 = column10;
		this.dynamicProperty = dynamicProperty;
	}

	public String getColumn1() {
		return Column1;
	}

	public void setColumn1(final String column1) {
		Column1 = column1;
	}

	public String getColumn2() {
		return Column2;
	}

	public void setColumn2(final String column2) {
		Column2 = column2;
	}

	public String getColumn3() {
		return Column3;
	}

	public void setColumn3(final String column3) {
		Column3 = column3;
	}

	public String getColumn4() {
		return Column4;
	}

	public void setColumn4(final String column4) {
		Column4 = column4;
	}

	public String getColumn5() {
		return Column5;
	}

	public void setColumn5(final String column5) {
		Column5 = column5;
	}

	public String getColumn6() {
		return Column6;
	}

	public void setColumn6(final String column6) {
		Column6 = column6;
	}

	public String getColumn7() {
		return Column7;
	}

	public void setColumn7(final String column7) {
		Column7 = column7;
	}

	public String getColumn8() {
		return Column8;
	}

	public void setColumn8(final String column8) {
		Column8 = column8;
	}

	public String getColumn9() {
		return Column9;
	}

	public void setColumn9(final String column9) {
		Column9 = column9;
	}

	public String getColumn10() {
		return Column10;
	}

	public void setColumn10(final String column10) {
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
	public void setDynamicProperty(final String dynamicProperty) {
		this.dynamicProperty = dynamicProperty;
	}

}
