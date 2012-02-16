package br.com.abril.nds.util;

import java.lang.reflect.Field;
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
	private transient List<Method> otherColumnsMethods;

	private transient static final String PREFIXO_METODO_GETTER = "get";
	
	private transient static final String PREFIXO_METODO_INEXISTENTE = "_";
	
	private int id;
	
	private String[] cell;
	
	
	/**
	 * Construtor que recebe como parâmetros o modelo a ser adequado,
	 * o VALOR da variavel de instância deste modelo que representa sua chave,
	 * e um varargs com o nome dos campos a serem apresentados no flexigrid.
	 * A ordem destes campos é importante.  Se o nome do campo iniciar com 
	 * "_" o valor deste sera 'settado' com uma string vazia.
	 * 
	 * 
	 * @param t
	 * @param idColumnValue
	 * @param includeCommomColumnNames
	 * @throws Exception
	 */
	public CellModel(T t, int idColumnValue, String... includeCommomColumnNames ) throws Exception {
		
		this.t = t;
		
		this.id = idColumnValue;
		
		otherColumnsMethods = new LinkedList<Method>();
		
		for (String columnName : includeCommomColumnNames) {
			
			if(columnName.startsWith(PREFIXO_METODO_INEXISTENTE)) {
				
				otherColumnsMethods.add(null);
				
			} else {
				
				Method m = getMethodFromFieldName(t, columnName);
				
				otherColumnsMethods.add(m);
				
			}
			
		}
		
		configureCell();
	}
	
	
	/**
	 * Construtor default.
	 */
	public CellModel(){}

	/**
	 * Utilitário que retorna Method a partir do nome do campo.
	 * 
	 * @param fieldName
	 * @param T t
	 * @return Method
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	private Method getMethodFromFieldName(T t, String fieldName) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		
		Field field = t.getClass().getDeclaredField(fieldName);
		
		String methodName =  (PREFIXO_METODO_GETTER + ( (""+field.getName().charAt(0)).toUpperCase()) +  field.getName().substring(1));
		
		return t.getClass().getMethod(methodName, null);
		
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

		String[] cellValues = new String[otherColumnsMethods.size()];
		
		int contador = 0;
		
		for (Method m : otherColumnsMethods) {
			
			if(m == null) {
				
				cellValues[contador] = "";
				
			} else {

				Object valor = t.getClass().getMethod(m.getName(), null).invoke(t, null);

				if (valor == null) {
					cellValues[contador] = "";
				} else {
					cellValues[contador] = valor.toString();
				}

				
			}
			
			contador++;

		}

		this.cell = cellValues;
	}
	
	
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
