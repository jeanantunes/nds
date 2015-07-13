package br.com.abril.nds.util.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation destinada à automatizar os processos mais comuns referentes a dados de rodapé, 
 * como somatória, contagem, média.
 * 
 * Utilização: <br/>
 * <br/>
 * @Footer(label="Sumarização", type=FooterType.SUM) <br/>
 * private BigDecimal field;<br/>
 * 
 * <br/>
 * 
 * Com isso o atributo "field" será sumarizado automáticamente, sem a necessidade de obter esses totais
 * antes de exportar os dados do Bean.
 * 
 * @author Discover Technology
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD, ElementType.METHOD})
public @interface Footer {

	String label() default "";
	
	String alignWithHeader() default "";
	
	FooterType type() default FooterType.SUM;
	
	boolean printVertical() default false;
	
	ColumnType columnType() default ColumnType.STRING;
}
