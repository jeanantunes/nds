package br.com.abril.nds.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import br.com.abril.nds.util.MathUtil;

/**
 * Classe de interceptação para eventos de entidades.
 * 
 * @author Discover Technology
 *
 */
public class EntityInterceptor extends EmptyInterceptor {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4809816440706721580L;
	
	/*
	 * (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
						  String[] propertyNames, Type[] types) {
		
		this.roundBigDecimalValues(entity);
		
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
	/*
	 * Arredonda valores do tipo {@link BigDecimal} do objeto passado como parâmetro.
	 * 
	 * @param object - objeto
	 */
	private void roundBigDecimalValues(Object object) {
		
		try {
		
			for (Field field : object.getClass().getDeclaredFields()) {
				
				if (BigDecimal.class.equals(field.getType())) {
					
					field.setAccessible(true);
					
					BigDecimal value = (BigDecimal) field.get(object);
					
					BigDecimal roundedValue = MathUtil.round(value, MathUtil.PADRAO_PRECISAO_DECIMAL);
					
					field.set(object, roundedValue);
				}
			}
			
		} catch (Exception exception) {
			
			throw new RuntimeException(exception);
		}
	}

}
