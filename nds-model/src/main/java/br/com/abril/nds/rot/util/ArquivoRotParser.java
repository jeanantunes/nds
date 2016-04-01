package br.com.abril.nds.rot.util;

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

public final class ArquivoRotParser {

	private boolean trace;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ArquivoRotParser.class);
	
	public ArquivoRotParser() {
		
	}
	
	public ArquivoRotParser(boolean debug) {
		this.trace = debug;
	}
	
	@SuppressWarnings("all")
	public void parseFTF(ArquivoRotBaseDTO rotBaseDTO, BufferedWriter parser) throws Exception {
		
		Class<? extends ArquivoRotBaseDTO> c = rotBaseDTO.getClass();
		
		Field []fields = c.getDeclaredFields();
		
		String delimiter=";";
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
				
				if (annot instanceof ArquivoRotfield) {
					
					Object obj = getter.invoke(rotBaseDTO, null);
					
					String value = (String) ((obj == null) ? "" : obj);
					
					ArquivoRotfield campo = (ArquivoRotfield)annot;
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
	
	@SuppressWarnings("all")
	public String parseArquivo(ArquivoRotBaseDTO ftfBaseDTO) throws Exception {
		
		StringBuilder retorno = new StringBuilder();
		
		Class<? extends ArquivoRotBaseDTO> c = ftfBaseDTO.getClass();
		
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
				
				if (annot instanceof ArquivoRotfield) {
					
					Object obj = getter.invoke(ftfBaseDTO, null);
					String value = "";
					try {
						
						value = (String) ((obj == null) ? "" : obj);
					} catch(Exception e) {
						
						value = (String) ((obj == null) ? "" : obj.toString());
					}
					
					ArquivoRotfield campo = (ArquivoRotfield)annot;
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
	
}
