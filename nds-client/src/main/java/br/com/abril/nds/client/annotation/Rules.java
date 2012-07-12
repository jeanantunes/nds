package br.com.abril.nds.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * Interface annotation de controle de permissões
 * @author InfoA2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Rules {

	Permissao value();
	
}
