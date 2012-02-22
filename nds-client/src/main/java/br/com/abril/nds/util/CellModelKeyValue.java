package br.com.abril.nds.util;

/**
 * Classe que abstrai um modelo genérico, sua serialização contempla a estrutura
 * esperada pelo plugin flexiGrid.
 * 
 * @author michel.jader
 *
 */
public class CellModelKeyValue<T> {
	
	private int id;
	private T cell;
	
	public CellModelKeyValue(int id, T t) {
		this.id = id;
		this.cell = t;
	}

	/**
	 * Obtém id
	 *
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Obtém cell
	 *
	 * @return T
	 */
	public T getCell() {
		return cell;
	}

	/**
	 * Atribuí cell
	 * @param cell 
	 */
	public void setCell(T cell) {
		this.cell = cell;
	}
	
}
