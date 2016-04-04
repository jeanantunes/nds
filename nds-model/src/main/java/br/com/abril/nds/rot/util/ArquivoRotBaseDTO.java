package br.com.abril.nds.rot.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoRotBaseDTO {
	
	public List<String> validateBean() {
		
		List<String> erros = new ArrayList<String>();
		
		Class<? extends ArquivoRotBaseDTO> c = this.getClass();
		
		Field[] fields = c.getDeclaredFields();
		
		for (Field field : fields) {
			
			Annotation[] annotations = field.getDeclaredAnnotations();
			
			for (Annotation annot: annotations) {
				
				if (annot instanceof ArquivoRotfield) {
					
					String value = null;
					try {
						value = (String) c.getMethod("get"+toFirstUpperCase(field.getName())).invoke(this);
					} catch (IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ArquivoRotfield campo = (ArquivoRotfield) annot;
					if (value != null && value.length() > campo.tamanho()) {
						if(!erros.contains(this.getClass().getName())) {
							erros.add(this.getClass().getName());
						}
						erros.add("campo: "+ field.getName() +" | valor: "+ value +" | tamanho: "+ value.length() +" | tam. esperado: "+ campo.tamanho());
					}
					
				}
			}
		}
		
		return erros;
	}
	
	private String toFirstUpperCase(String string) {
		
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}
	
}