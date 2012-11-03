package br.com.abril.nds.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.ManyToAny;

import br.com.abril.nds.dto.auditoria.AtributoDTO;
import br.com.abril.nds.dto.auditoria.AuditoriaDTO;
import br.com.abril.nds.util.TipoOperacaoSQL;
import br.com.caelum.vraptor.Path;

public class AuditoriaUtil {

	private static final String EXCLUDED_FIELD = "serialVersionUID";
	
	private static final String CONTROLLER_SUFIX = "Controller";
	
	private static final String NDS_ROOT_PACKAGE = "br.com.abril.nds";
	
	private static final String CONTROLLERS_PACKAGE = "br.com.abril.nds.controllers";
	
	private static final String SLASH = "/";

	public static AuditoriaDTO generateAuditoriaDTO(Object newEntity, Object oldEntity, String entityType, Thread currentThread, 
													Object user, TipoOperacaoSQL tipoOperacaoAuditoria) {
		
		AuditoriaDTO auditoria = new AuditoriaDTO();

		Class<?> oldEntityClass = oldEntity != null ? oldEntity.getClass() : null;
		Class<?> newEntityClass = newEntity != null ? newEntity.getClass() : null;
		
		auditoria.setDadosAntigos(AuditoriaUtil.entityToDTO(oldEntity, oldEntityClass));
		auditoria.setDadosNovos(AuditoriaUtil.entityToDTO(newEntity, newEntityClass));
		auditoria.setDataAuditoria(new Date());
		auditoria.setEntidadeAuditada(entityType);
		auditoria.setNdsStackTrace(AuditoriaUtil.getNDSStackTrace(currentThread));
		auditoria.setTipoOperacaoAuditoria(tipoOperacaoAuditoria);
		auditoria.setUrlAcesso(AuditoriaUtil.getURLFromController(currentThread));
		auditoria.setUsuario(user);
		
		return auditoria;
	}
	
	private static List<AtributoDTO> entityToDTO(Object entity, Class<?> clazz) {

		if (entity == null) {

			return null;
		}

		List<AtributoDTO> result = new ArrayList<AtributoDTO>();

		if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {

			result.addAll(entityToDTO(entity, clazz.getSuperclass()));
		}

		List<Field> fields = getValidFields(clazz);

		List<Field> embeddedFields = getEmbeddedEntityFields(clazz); 

		result.addAll(getEntityDTO(fields, entity));

		result.addAll(getEmbbededEntityDTO(embeddedFields, entity));

		return result;
	}
	
	private static List<AtributoDTO> getEmbbededEntityDTO(List<Field> fields, Object entity) {
		
		List<AtributoDTO> result = new ArrayList<AtributoDTO>();
		
		for (Field field : fields) {
			
			try {

				field.setAccessible(true);
				
				if (field.get(entity) == null) {
					
					continue;
				}

				result.addAll(entityToDTO(field.get(entity), field.getType()));
				
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		
		return result;
	}

	private static List<AtributoDTO> getEntityDTO(List<Field> fields, Object entity) {

		List<AtributoDTO> result = new ArrayList<AtributoDTO>();

		for (Field field : fields) {

			try {

				field.setAccessible(true);

				AtributoDTO atributo = new AtributoDTO();

				atributo.setNome(field.getName());

				atributo.setValor(field.get(entity));

				result.add(atributo);
				
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
		}

		return result;
	}
	
	private static String getURLFromController(Thread currentThread) {

		String url = "";

		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();

		for (StackTraceElement stackTraceElement : stackTraceElements) {

			String methodName = stackTraceElement.getMethodName();

			String fullyQualifiedClassName = stackTraceElement.getClassName();

			if (fullyQualifiedClassName.endsWith(CONTROLLER_SUFIX) &&
					fullyQualifiedClassName.startsWith(CONTROLLERS_PACKAGE)) {

				String pathValueFromClass = getPathValueFromClass(fullyQualifiedClassName);

				String pathValueFromMethod = getPathValueFromMethod(fullyQualifiedClassName, methodName);

				url = pathValueFromClass 
					+ (pathValueFromClass.endsWith(SLASH) || pathValueFromMethod.startsWith(SLASH) 
							? "" : SLASH)
					+ ((pathValueFromMethod == null || pathValueFromMethod.isEmpty()) ? methodName : pathValueFromMethod);
				
				break;
			}
		}

		return url;
	}

	private static List<String> getNDSStackTrace(Thread currentThread) {

		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
		
		List<String> ndsStacktrace = new ArrayList<String>();
		
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			
			if (stackTraceElement.getClassName().startsWith(NDS_ROOT_PACKAGE)) {

				ndsStacktrace.add(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
			}
		}

		return ndsStacktrace;
	}
	
	private static List<Field> getValidFields(Class<?> clazz) {

		Field[] allFields = clazz.getDeclaredFields();
		
		List<Field> validFields = new ArrayList<Field>();

		for (Field field : allFields) { 

			if (!field.getName().equals(EXCLUDED_FIELD)
					&& !field.isAnnotationPresent(ManyToAny.class)
					&& !field.isAnnotationPresent(ManyToMany.class)
					&& !field.isAnnotationPresent(ManyToOne.class)
					&& !field.isAnnotationPresent(Transient.class)
					&& !field.isAnnotationPresent(Embedded.class)
					&& !field.isAnnotationPresent(EmbeddedId.class)
					&& !field.isAnnotationPresent(OneToOne.class)
					&& !field.isAnnotationPresent(OneToMany.class)) {
				
				validFields.add(field);
			}
		}

		return validFields;
	}
	
	private static List<Field> getEmbeddedEntityFields(Class<?> clazz) {
		
		List<Field> fields = new ArrayList<Field>();
		
		for (Field field : clazz.getDeclaredFields()) {

			if (!EXCLUDED_FIELD.equals(field.getName())
					&& (field.isAnnotationPresent(Embedded.class)
							|| field.isAnnotationPresent(EmbeddedId.class))
//							|| field.isAnnotationPresent(OneToOne.class)
//							|| field.isAnnotationPresent(ManyToOne.class)
							) {

				fields.add(field);
			}
		}
		
		return fields;
	}

	private static String getPathValueFromClass(String fullyQualifiedClassName) {

		try {

			Class<?> clazz = Class.forName(fullyQualifiedClassName);

			String pathValue = "";
			
			if (clazz.isAnnotationPresent(Path.class)) {
				
				Path path = clazz.getAnnotation(Path.class);
				
				pathValue = path.value()[0];
			}

			return pathValue;

		} catch (ClassNotFoundException e) {

			return null;
		}
	}
	
	private static String getPathValueFromMethod(String fullyQualifiedClassName, String methodName) {

		try {

			Class<?> clazz = Class.forName(fullyQualifiedClassName);

			Method[] methods = clazz.getMethods();

			String pathValue = "";

			for (Method method : methods) {

				if (method.getName().equals(methodName) 
						&& method.isAnnotationPresent(Path.class)) {
					
					Path path = method.getAnnotation(Path.class);

					pathValue = path.value()[0];
							    
					break;
				}
			}

			return pathValue;

		} catch (ClassNotFoundException e) {

			return null;
		}
	}
}
