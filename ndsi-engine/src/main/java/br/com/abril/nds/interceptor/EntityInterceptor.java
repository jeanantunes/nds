package br.com.abril.nds.interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

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
		
		
		if(entity instanceof PessoaJuridica){
			PessoaJuridica pessoaJuridica = (PessoaJuridica) entity;
			pessoaJuridica.removeMaskCnpj();
		}

		return false;
	}
	
}
