package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.abril.nds.util.TipoSecao;

/**
 * Anotação que identifica um atributo da Nota Fiscal Eletrônica que pode ser
 * exportável.
 * 
 * @author Discover Technology
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface NFEExport {

	/**
	 * Nome da seção da linha.
	 * 
	 * @return
	 */
	public TipoSecao secao();
	
	/**
	 * Tamanho máximo de caracteres.
	 * 
	 * @return
	 */
	public int tamanho() default 0;
	
	/**
	 * Posição do campo na seção.
	 * Obs: A primeira posição é o 0 (zero) após o nome da seção.
	 * 
	 * @return
	 */
	public int posicao(); 
	
	/**
	 * Máscara de formatação que não seja a mascara padrão do tipo.
	 * 
	 * @return
	 */
	public String mascara() default "";
	
	/**
	 * Atributo para informar o tipo de documento (CPF/CNPJ)
	 * @return
	 */
	public String documento() default "";
	
}
