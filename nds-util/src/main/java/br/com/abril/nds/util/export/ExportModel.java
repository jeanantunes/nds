package br.com.abril.nds.util.export;

import java.util.List;

public class ExportModel {
	
	private List<ExportFilter> filters;
	
	private List<ExportHeader> headers;
	
	private List<ExportRow> rows;
	
	private List<ExportFooter> footers;
	
	/**
	 * Construtor padrão.
	 */
	public ExportModel() {
		
	}

	/**
	 * Construtor.
	 * 
	 * @param filters - filtros
	 * @param headers - cabeçalhos
	 * @param rows - linhas
	 */
	public ExportModel(List<ExportFilter> filters, 
					   List<ExportHeader> headers,
					   List<ExportRow> rows) {
		
		this.filters = filters;
		this.headers = headers;
		this.rows = rows;
	}

	/**
	 * @return the headers
	 */
	public List<ExportHeader> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<ExportHeader> headers) {
		this.headers = headers;
	}

	/**
	 * @return the rows
	 */
	public List<ExportRow> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<ExportRow> rows) {
		this.rows = rows;
	}

	/**
	 * @return the filters
	 */
	public List<ExportFilter> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(List<ExportFilter> filters) {
		this.filters = filters;
	}

	/**
	 * @return the footers
	 */
	public List<ExportFooter> getFooters() {
		return footers;
	}

	/**
	 * @param footers the footers to set
	 */
	public void setFooters(List<ExportFooter> footers) {
		this.footers = footers;
	}
	
}
