package br.com.abril.nds.util;


/**
 * Classe que abstrai um modelo genérico, sua serialização contempla a estrutura
 * esperada pelo plugin flexiGrid.
 * 
 * @author michel.jader
 *
 */
public class CellModel {
	
	private int id;
	
	private String[] cell;
	
	/**
	 * Construtor que recebe o id a ser atribuido a linha no flexiGrid e um 
	 * varargs com os valores da célula.
	 * 
	 * @param idColumnValue
	 * @param includeColumnValues
	 */
	public CellModel(int idColumnValue, String... includeColumnValues ) {
		
		this.id = idColumnValue;
		
		if (includeColumnValues != null){
			for (int indice = 0 ; indice < includeColumnValues.length ; indice++){
				if (includeColumnValues[indice] == null){
					includeColumnValues[indice] = "";
				}
			}
		}
		
		this.cell = includeColumnValues;
		
	}
	
	
	/**
	 * Construtor default.
	 */
	public CellModel(){}

	
	/**
	 * Obtém o id
	 *  
	 * @return int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Obtém com os valores da linha da grid.
	 * 
	 * @return
	 */
	public String[] getCell() {
		return cell;
	}

	/**
	 * Atribui o id.
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Atribui o cell
	 * * @param cell 
	 */
	public void setCell(String[] cell) {
		this.cell = cell;
	}
	
	
}
