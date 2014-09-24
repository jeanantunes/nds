package br.com.abril.nds.ftfutil;

import java.io.BufferedWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.util.export.Delimiter;

public final class FTFParser {

	private boolean trace;
	
	private static Logger LOGGER = LoggerFactory.getLogger(FTFParser.class);
	
	public FTFParser() {
		
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
		
		if(delimiter != null) {
			
			parser.append(StringUtils.join(campos, delimiter));
			
		} else {
			
			for (String campo : campos) {
				
				if (isTrace()) {
					System.out.println(campo);
				}
				
				if(campo != null) {
				    
					if(!Normalizer.isNormalized(campo, Form.NFD)) {
						
						campo = Normalizer.normalize(campo, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
					}
				    
					parser.append(campo);
				}
			}
			
		}
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
		case 1:
		case 9:
			className="br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro0"+ Integer.parseInt(tipoRegistro);
			break;
		
		default:
			break;
		}
		
		Object newInstance = parseLinhaPeloClassName(line, className);
		
		return (FTFBaseDTO) newInstance;
			
	}
	
	public static FTFBaseDTO parseLinhaEnvioFTF(String line) throws Exception {
		
		String tipoRegistro = line.substring(0, 1);

		String className = "";
		switch (Integer.parseInt(tipoRegistro)) {
		case 0:
		case 1:
		case 2:
		case 5:
		case 8:
		case 9:
			className="br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro0"+ Integer.parseInt(tipoRegistro);
			break;

		default:
			break;
		}
		
		Object newInstance = parseLinhaPeloClassName(line, className);
		
		return (FTFBaseDTO) newInstance;
		
	}

	private static Object parseLinhaPeloClassName(String line, String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		
		Class<?> forName = Class.forName(className);
		Method[] methods = forName.getDeclaredMethods();
		Field[] fields = forName.getDeclaredFields();
		
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
		
		int count = 0;
		for (Method method : methodList) {
			count += method.getAnnotation(FTFfield.class).tamanho();
		}
		
		if(methodList.isEmpty()) {
			
			for (Field field : fields) {
				
				Annotation[] annotations = field.getDeclaredAnnotations();
				
				for (Annotation annot: annotations) {
					
					if (annot instanceof FTFfield) {
						
						FTFfield campo = (FTFfield) annot;
						count += campo.tamanho();
						
					}
				}
			}
			
		}
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(className + " || Previsto: "+ StringUtils.rightPad(String.valueOf(count), 4, ' ') 
					+" / Encontrado: "+ StringUtils.rightPad(String.valueOf(line.length()), 4, ' ') 
					+" | "+ line.toString());
		}
		
		if(count != line.length()) {
			throw new RuntimeException("Linha do arquivo de retorno com tamanho diferente do mapeamento.");
			
		}
		
		Object newInstance = forName.newInstance();
		
		int pointer = 0;
		for (Method m : methodList) {
			int tamanho = m.getAnnotation(FTFfield.class).tamanho();
			String tipo = m.getAnnotation(FTFfield.class).tipo();
			String value = line.substring(pointer, pointer + tamanho);
			
			Object param = null;
			
			if(tipo.equalsIgnoreCase("long")) {
				param = Long.valueOf(value != null ? value.trim() : "0");
			} else {
				param = value;
			}
			
			m.invoke(newInstance, param);
			pointer += tamanho;
			
		}
		return newInstance;
	}
	
}