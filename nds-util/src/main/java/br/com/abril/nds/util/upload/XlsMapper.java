package br.com.abril.nds.util.upload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Thiago
 * Anotação criada para mapear a coluna do XLS com o Bean desejado.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface XlsMapper {
	
	String value();
	
}
