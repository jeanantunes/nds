package br.com.abril.nds.ftfutil;

import java.io.BufferedWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.Delimiter;

public final class FTFParser {

	private boolean trace;
	
	public FTFParser() {
		// TODO Auto-generated constructor stub
	}
	
	public FTFParser(boolean debug) {
		
		this.trace = debug;
	}
	
	@SuppressWarnings("all")
	public void parseFTF(FTFBaseDTO ftfBaseDTO, BufferedWriter parser) throws Exception {
		
		Class<? extends FTFBaseDTO> c = ftfBaseDTO.getClass();
		
		Field []fields = c.getDeclaredFields();
		
		String delimiter=null;
		for (Annotation a : c.getAnnotations()) {
			if(a instanceof Delimiter){
				delimiter = ((Delimiter) a).value();
				break;
			}
		}
		Method getter = null;
		
		String campos[] = new String[fields.length];
		
		for (Field field:fields) {
			
			Annotation[] annotations = field.getDeclaredAnnotations();
			
			getter = getMethodGetter(c, field);
			
			for (Annotation annot: annotations) {
				
				if (annot instanceof FTFfield) {
					
					Object obj = getter.invoke(ftfBaseDTO, null);
					
					String value = (String) ((obj == null) ? "" : obj);
					
					FTFfield campo = (FTFfield)annot;
					if (value.length() > campo.tamanho()) {
						
						value = value.substring(0,campo.tamanho());
					}
					else if (value.length() < campo.tamanho()) {
						
						value = (campo.tipo().equals("numeric")) ? StringUtils.leftPad(value, campo.tamanho(), '0'):
							completaComEspaco(value, campo.tamanho());
					}
					
					campos[campo.ordem()-1] = value;
					
				}
			}
		}
		if(delimiter!=null){
			parser.append(StringUtils.join(campos,delimiter));
		}else{
			for (String campo:campos) {
				
				if (isTrace()) {
					
					System.out.println(campo);
				}
				
				parser.append(campo);
			}
			
		}
	}
	
	@SuppressWarnings("all")
	public String parseArquivo(FTFBaseDTO ftfBaseDTO) throws Exception {
		
		StringBuilder retorno = new StringBuilder();
		
		Class<? extends FTFBaseDTO> c = ftfBaseDTO.getClass();
		
		Field []fields = c.getDeclaredFields();
		
		String delimiter=null;
		for (Annotation a : c.getAnnotations()) {
			if(a instanceof Delimiter){
				delimiter = ((Delimiter) a).value();
				break;
			}
		}
		Method getter = null;
		
		String campos[] = new String[fields.length];
		
		for (Field field:fields) {
			
			Annotation[] annotations = field.getDeclaredAnnotations();
			
			getter = getMethodGetter(c, field);
			
			for (Annotation annot: annotations) {
				
				if (annot instanceof FTFfield) {
					
					Object obj = getter.invoke(ftfBaseDTO, null);
					
					String value = (String) ((obj == null) ? "" : obj);
					
					FTFfield campo = (FTFfield)annot;
					if (value.length() > campo.tamanho()) {
						
						value = value.substring(0,campo.tamanho());
					}
					else if (value.length() < campo.tamanho()) {
						
						value = (campo.tipo().equals("numeric")) ? StringUtils.leftPad(value, campo.tamanho(), '0'):
							completaComEspaco(value, campo.tamanho());
					}
					
					campos[campo.ordem()-1] = value;
					
				}
			}
		}
		if(delimiter!=null){
			
			retorno.append(StringUtils.join(campos,delimiter));
			
		}else{
			for (String campo:campos) {
				
				if (isTrace()) {
					
					System.out.println(campo);
				}
				
				retorno.append(campo);
			}
		}

		return retorno.toString();
	}
	
	private String completaComEspaco(String value, int tamanho) {
		
		StringBuilder espaco = new StringBuilder(value);
		
		if (value.length() < tamanho) {
			
			tamanho -= value.length();
			
			for (int i=1; i <= tamanho; i++) {
				espaco.append(" ");
			}
		}
		
		return espaco.toString();
	}
	
	@SuppressWarnings("all")
	private Method getMethodGetter(Class c, Field field) throws Exception {
		
		return c.getMethod("get"+toFirstUpperCase(field.getName()), null);
	}
	
	private String toFirstUpperCase(String string) {
		
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}
	
	
	public static FTFBaseDTO parseLinhaRetornoFTF(String line) throws Exception {
		
		String tipoRegistro = line.substring(0, 1);

		String className = "";
		switch (Integer.parseInt(tipoRegistro)) {
		case 0:
			className="br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro00";
			break;
		case 1:
			className="br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01";
			break;
		case 9:
			className="br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro09";
			break;
		default:
			break;
		}
		
		Class<?> forName = Class.forName(className);
		Method[] methods = forName.getDeclaredMethods();
		List<Method> methodList = new ArrayList<Method>();
		
		for (Method method : methods) {
			
			if(method.getName().startsWith("set") && method.isAnnotationPresent(FTFfield.class)){
				methodList.add(method);
			}
				
		}
		
		Collections.sort(methodList,new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				int ordemM1 = m1.getAnnotation(FTFfield.class).ordem();
				int ordemM2 = m2.getAnnotation(FTFfield.class).ordem();
				
				return Integer.compare(ordemM1, ordemM2);
			}
		
		} );
		
		int count=0;
		for (Method method : methodList) {
			count+=method.getAnnotation(FTFfield.class).tamanho();
		}
		
		if(count!=line.length()){
			throw new RuntimeException("Linha do arquivo de retorno com tamanho diferente do mapeamento.");
		}
		
		
		Object newInstance = forName.newInstance();
		
		int pointer=0;
		for (Method m : methodList) {
			int tamanho = m.getAnnotation(FTFfield.class).tamanho();
			String tipo = m.getAnnotation(FTFfield.class).tipo();
			String value = line.substring(pointer, pointer+tamanho);
			
			Object param = null;
			
			if(tipo.equalsIgnoreCase("long")){
				param = new Long(value);
			}else{
				param = value;
				
			}
			
			m.invoke(newInstance, param);
			pointer+=tamanho;
			
		}
		
		return (FTFBaseDTO)newInstance;
			
	}
			
}
