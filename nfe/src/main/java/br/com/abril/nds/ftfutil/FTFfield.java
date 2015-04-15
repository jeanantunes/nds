package br.com.abril.nds.ftfutil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface FTFfield {

	public int tamanho();
	
	public String tipo();
	
	public int ordem();
}
