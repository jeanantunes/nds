package br.com.abril.nds.util;

/**
 * Classe que abstrai o modelo utilizado pelo
 * plugin flexigrid com dados da pagina, total de registros
 * e os valores a serem apresentados.
 * 
 * @author michel.jader
 *
 * @param <T>
 */
public class TableModel<T> {

	private int page;
	
	private int total;
	
	private CellModel<T>[] rows;

	/**
	 * Obtém page
	 *
	 * @return int
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Atribuí page
	 * @param page 
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Obtém total
	 *
	 * @return int
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Atribuí total
	 * @param total 
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Obtém rows
	 *
	 * @return CellModel<T>[]
	 */
	public CellModel<T>[] getRows() {
		return rows;
	}

	/**
	 * Atribuí rows
	 * @param rows 
	 */
	public void setRows(CellModel<T>[] rows) {
		this.rows = rows;
	}

	
	
	
}
