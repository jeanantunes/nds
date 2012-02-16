package br.com.abril.nds.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que abstrai um modelo genérico, sua serialização contempla a estrutura
 * esperada pelo plugin flexiGrid.
 * 
 * @author michel.jader
 *
 * @param <T>
 */
public class CellModel<T> {

	private transient T t;
	private transient Method idColumnMethod;
	private transient List<Method> otherColumnsMethods;

	private static final String PREFIXO__METODO_GETTER = "get";
	
	private int id;
	
	private String[] cell;
	
	/**
	 * Construtor que recebe como parâmetros o modelo a ser adequado,
	 * o nome da variavel de instância deste modelo que representa sua chave,
	 * e um varargs com o nome dos campos a serem apresentados no flexigrid.
	 * A ordem destes campos é importante. 
	 * 
	 * Se a flag generateValuesWithColumnsName for false os valores não serão
	 * gerados automaticamente.
	 * 
	 * 
	 * @param t
	 * @param idColumnName
	 * @param includeCommomColumnNames
	 * @throws Exception
	 */
	public CellModel(T t, String idColumnName, String... includeCommomColumnNames) throws Exception {
		
		this.t = t;

		this.idColumnMethod = this.t.getClass().getMethod(getMethodNameFromField(idColumnName), null);

		otherColumnsMethods = new LinkedList<Method>();
		
		for (String columnName : includeCommomColumnNames) {
			
			Method m = this.t.getClass().getMethod(getMethodNameFromField(columnName), null);

			otherColumnsMethods.add(m);
			
		}

		configureId();
		
		configureCell();
		
	}
	
	/**
	 * Construtor default.
	 */
	public CellModel(){}

	/**
	 * Utilitário que retorna o nome do getter a partir do nome do campo.
	 * 
	 * @param fieldName
	 * @return String
	 */
	private String getMethodNameFromField(String fieldName) {
		String methodName =  (PREFIXO__METODO_GETTER + ( (""+fieldName.charAt(0)).toUpperCase()) +  fieldName.substring(1));
		return methodName;
	}
	
	/**
	 * Método que 'setta' o id deste objeto.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void configureId() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		this.id = (Integer) t.getClass().getMethod(idColumnMethod.getName(), null).invoke(t, null);
	}

	/**
	 * Método que seta os campos a serem apresentados no flexigrid.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void configureCell() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		String[] cellValues = new String[otherColumnsMethods.size()+1];
		
		cellValues[0] = ""+getId();
		
		int contador = 1;
		
		for (Method m : otherColumnsMethods) {

			Object valor = t.getClass().getMethod(m.getName(), null).invoke(t, null);

			if (valor == null) {
				cellValues[contador] = "";
			} else {
				cellValues[contador] = valor.toString();
			}
			
			contador++;

		}

		this.cell = cellValues;
	}
	
	
	/**
	 * Obtém o id.
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	
	/**
	 * Obtém o cell com os valores da linha da grid.
	 * 
	 * @return
	 */
	public String[] getCell() {
		return cell;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Atribuí cell
	 * @param cell 
	 */
	public void setCell(String[] cell) {
		this.cell = cell;
	}

	
	
	
	
	
}
