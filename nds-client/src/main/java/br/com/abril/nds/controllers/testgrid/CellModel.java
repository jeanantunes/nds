package br.com.abril.nds.controllers.testgrid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CellModel<T> {

	private transient T t;
	private transient Method idColumnMethod;
	private transient List<Method> otherColumnsMethods;

	private int id;
	
	private String[] cell;
	
	public CellModel(T t) throws Exception {

		this.t = t;

		Method[] methods = this.t.getClass().getMethods();

		otherColumnsMethods = new ArrayList<Method>();
		
		System.out.println(t.getClass().getName());
		
		for (Method m : methods) {

			if (m.getName().equals(("getId" + t.getClass().getSimpleName()))) {
				this.idColumnMethod = m;
				
			} else if (!m.getName().contains("getClass") && m.getName().contains("get") && m.getReturnType() != void.class) {
				
				this.otherColumnsMethods.add(m);
			
			}

		}

		if (this.idColumnMethod == null || this.otherColumnsMethods.isEmpty()) {
			throw new NoSuchMethodException(
					"METODO GET ID OU OUTROS METODOS NAO ENCONTRADO");
		}

		configureId();
		
		configureCell();
		
		
	}


	public void configureId() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		this.id = (Integer) t.getClass().getMethod(idColumnMethod.getName(), null).invoke(t, null);
	}

	
	public void configureCell() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		String[] cellValues = new String[otherColumnsMethods.size()+1];
		
		
		cellValues[0] = getId()+"";
		
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

	

	public int getId() {
		return id;
	}


	public String[] getCell() {
		return cell;
	}

	
	
	
}
