package br.com.abril.nds.interceptor;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

import javax.mail.Session;

import org.hibernate.EmptyInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditLogInterceptor extends EmptyInterceptor {

	Session session;

	boolean onDelete(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types) {

		setValue(currentState, propertyNames, "updatedBy",
				SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal());
		setValue(currentState, propertyNames, "updatedOn", new Date());
		return true;
	}

	boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		setValue(state, propertyNames, "createdBy", SecurityContextHolder
				.getContext().getAuthentication().getPrincipal());
		setValue(state, propertyNames, "createdOn", new Date());
		return true;
	}

	private void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value) {
		//propertyNames.toList().indexOf(propertyToSet)
		/*if (index >= 0) {
			currentState[index] = value
		}*/	
	}
}