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
	 * Obt�m page
	 *
	 * @return int
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Atribu� page
	 * @param page 
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Obt�m total
	 *
	 * @return int
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Atribu� total
	 * @param total 
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Obt�m rows
	 *
	 * @return CellModel<T>[]
	 */
	public CellModel<T>[] getRows() {
		return rows;
	}

	/**
	 * Atribu� rows
	 * @param rows 
	 */
	public void setRows(CellModel<T>[] rows) {
		this.rows = rows;
	}

	
	
	
}
