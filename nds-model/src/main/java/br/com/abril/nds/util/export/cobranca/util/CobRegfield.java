package br.com.abril.nds.util.export.cobranca.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface CobRegfield {

	public int tamanho();
	
	public String tipo();
	
	public int ordem();
}
