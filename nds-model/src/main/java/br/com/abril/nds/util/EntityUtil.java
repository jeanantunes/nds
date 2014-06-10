package br.com.abril.nds.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;

public class EntityUtil {

	/**
	 * Obtém uma instancia duplicado do objeto passado por parâmetro. Se a
	 * entidade estiver mapeada no JPA, é atribuido nulo ao ID da entidade.
	 * 
	 * @param entidade
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public static <E> E clonarSemID(E entidade)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException {
		
		E entidadeClonada = (E)BeanUtils.cloneBean(entidade);
		
		List<Field> campos  = Arrays.asList(entidadeClonada.getClass().getDeclaredFields());
		
		for (Field campo : campos) {
			Id idAnnotation = campo.getAnnotation(Id.class);
			EmbeddedId embeddedIdAnnotation = campo.getAnnotation(EmbeddedId.class);

			if (idAnnotation != null || embeddedIdAnnotation != null) {
				campo.setAccessible(true);
				campo.set(entidadeClonada, null);
			}
		}
		
		return entidadeClonada;
	}
	
}
