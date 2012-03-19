package br.com.abril.nds.util.export;

import java.util.List;

public class ExportRow {

	private List<ExportColumn> columns;
	
	public ExportRow() {
		
	}
	
	public ExportRow(List<ExportColumn> columns) {
		
		this.columns = columns;
	}

	/**
	 * @return the columns
	 */
	public List<ExportColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ExportColumn> columns) {
		this.columns = columns;
	}
	
}
