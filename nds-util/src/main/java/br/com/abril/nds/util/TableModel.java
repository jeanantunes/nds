package br.com.abril.nds.util;

import java.io.Serializable;
import java.util.List;

/**
 * Classe que abstrai o modelo utilizado pelo
 * plugin flexigrid com dados da pagina, total de registros
 * e os valores a serem apresentados.
 * 
 * @author michel.jader
 */
public class TableModel<T> implements Serializable {

	private static final long serialVersionUID = 2984213137565719068L;

	private int page;
	
	private int total;
	
	private List<T> rows;
	
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
	 * Obtém rows
	 *
	 * @return List<CellModel>
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * Atribuí rows
	 * @param rows 
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
