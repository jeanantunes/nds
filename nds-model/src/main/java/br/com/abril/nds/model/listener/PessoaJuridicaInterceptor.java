package br.com.abril.nds.model.listener;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class PessoaJuridicaInterceptor  extends EmptyInterceptor {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3413361950072385087L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		 if ( entity instanceof PessoaJuridica ) {
	           System.out.println("PessoaJuridica :"  + entity);
	        }
	        return true;
	}
	
}
