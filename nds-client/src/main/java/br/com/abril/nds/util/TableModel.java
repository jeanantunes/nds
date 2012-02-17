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
	
	private CellModel[] rows;

	/**
	 * Obtém a page.
	 * 
	 * @return int
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Atribuí a page
	 * @param page 
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Obtém o total
	 * 
	 * @return int
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Atribuí o total
	 * @param total 
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Obtem cellModel
	 *
	 * @return CellModel<T>[]
	 */
	public CellModel[] getRows() {
		return rows;
	}

	/**
	 * Atribui rows
	 * @param rows 
	 */
	public void setRows(CellModel[] rows) {
		this.rows = rows;
	}

	
	
	
}
