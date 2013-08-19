package br.com.abril.nds.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que abstrai um modelo genérico, sua serialização contempla a estrutura
 * esperada pelo plugin flexiGrid.
 * 
 * @author michel.jader
 *
 */
public class CellModelKeyValue<T> implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
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
	
	/**
	 * Gera a lista de {@link CellModelKeyValue} a partir da
	 * lista do tipo encapsulado, gerando um identificador
	 * interno para cada linha.
	 * @param rows lista do tipo  encapsulado
	 * @return lista de {@link CellModelKeyValue} do tipo encapsulado
	 * 
	 */
	public static <T> List<CellModelKeyValue<T>> toCellModelKeyValue(
			List<T> rows) {
		List<CellModelKeyValue<T>> cells = new ArrayList<CellModelKeyValue<T>>(
				rows.size());
		int i = 0;
		for (T row : rows) {
			cells.add(new CellModelKeyValue<T>(++i, row));
		}
		return cells;
	}
	
}
