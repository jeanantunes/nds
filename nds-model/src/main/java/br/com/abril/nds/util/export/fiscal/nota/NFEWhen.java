package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface NFEWhen {
	public NFEConditions condition();
	
	public NFEExport export();
}
