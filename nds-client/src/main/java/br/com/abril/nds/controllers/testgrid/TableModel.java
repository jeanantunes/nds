package br.com.abril.nds.controllers.testgrid;

public class TableModel {

	private int page;
	
	private int total;
	
	private CellModel[] rows;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public CellModel[] getRows() {
		return rows;
	}

	public void setRows(CellModel[] rows) {
		this.rows = rows;
	}
	
}
