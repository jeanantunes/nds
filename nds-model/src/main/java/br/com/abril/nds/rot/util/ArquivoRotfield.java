package br.com.abril.nds.rot.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ArquivoRotfield {

	public int tamanho();
	
	public String tipo();
	
	public int ordem();
}
