package br.com.abril.nds.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

import org.hibernate.annotations.ManyToAny;

import br.com.abril.nds.dto.auditoria.AtributoDTO;
import br.com.abril.nds.dto.auditoria.AuditoriaDTO;
import br.com.abril.nds.util.TipoOperacaoSQL;
import br.com.caelum.vraptor.Path;

public class AuditoriaUtil {

	private static final String GETTER_PREFIX = "get";
	
	private static final String IS_PREFIX = "is";

	private static final String EXCLUDED_FIELD = "serialVersionUID";
	
	private static final String CONTROLLER_SUFIX = "Controller";
	
	private static final String NDS_ROOT_PACKAGE = "br.com.abril.nds";
	
	private static final String CONTROLLERS_PACKAGE = "br.com.abril.nds.controllers";
	
	private static final String SLASH = "/";

	public static AuditoriaDTO generateAuditoriaDTO(Object newEntity, Object oldEntity, String entityType, Thread currentThread, 
													Object user, TipoOperacaoSQL tipoOperacaoAuditoria) {
		
		AuditoriaDTO auditoria = new AuditoriaDTO();

		auditoria.setDadosAntigos(AuditoriaUtil.entityToDTO(oldEntity));
		auditoria.setDadosNovos(AuditoriaUtil.entityToDTO(newEntity));
		auditoria.setDataAuditoria(new Date());
		auditoria.setEntidadeAuditada(entityType);
		auditoria.setNdsStackTrace(AuditoriaUtil.getNDSStackTrace(currentThread));
		auditoria.setTipoOperacaoAuditoria(tipoOperacaoAuditoria);
		auditoria.setUrlAcesso(AuditoriaUtil.getURLFromController(currentThread));
		auditoria.setUsuario(user);
		
		return auditoria;
	}
	
	private static List<AtributoDTO> entityToDTO(Object entity) {

		if (entity == null) {
			
			return null;
		}
		
		List<AtributoDTO> result = new ArrayList<AtributoDTO>();

		List<String> embeddedFieldsName = getEmbeddedEntityFields(entity); 

		List<Method> methods = getValidMethods(entity);

		for (Method method : methods) {

			try {

				if (embeddedFieldsName.contains(
						method.getName().replaceFirst(GETTER_PREFIX, ""))) {

					result.addAll(entityToDTO(method.invoke(entity, new Object[0])));
				}

				AtributoDTO atributo = new AtributoDTO();

				String atributeName = method.getName().startsWith(GETTER_PREFIX) ?
							method.getName().replaceFirst(GETTER_PREFIX, "") : 
							method.getName().replaceFirst(IS_PREFIX, "");
				
				atributo.setNome(atributeName);
				atributo.setValor(method.invoke(entity, new Object[0]));

				result.add(atributo);

			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
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
	
	private static List<Method> getValidMethods(Object entity) {

		List<Method> validMethods = new ArrayList<Method>();

		Field[] allFields = entity.getClass().getDeclaredFields();

		for (Field field : allFields) { 

			if (!field.getName().equals(EXCLUDED_FIELD)
					&& !field.isAnnotationPresent(ManyToAny.class)
					&& !field.isAnnotationPresent(ManyToMany.class)
					&& !field.isAnnotationPresent(ManyToOne.class)) {

				for (Method method : entity.getClass().getMethods()) {

					if (method.getName().endsWith(upperCaseFirstLetter(field.getName()))) {

						validMethods.add(method);
						
						break;
					}
				}
			}
		}

		return validMethods;
	}
	
	private static List<String> getEmbeddedEntityFields(Object entity) {
		
		List<String> fields = new ArrayList<String>();
		
		for (Field field : entity.getClass().getDeclaredFields()) {

			if (!EXCLUDED_FIELD.equals(field.getName())
					&& (field.isAnnotationPresent(Embedded.class)
							|| field.isAnnotationPresent(EmbeddedId.class))
							|| field.isAnnotationPresent(OneToOne.class)
							|| field.isAnnotationPresent(OneToMany.class)) {

				fields.add(upperCaseFirstLetter(field.getName()));
			}
		}
		
		return fields;
	}

	private static String upperCaseFirstLetter(String str) {

		return str.replace(
			String.valueOf(str.charAt(0)), 
			String.valueOf(str.charAt(0)).toUpperCase()
		);
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
