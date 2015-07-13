package br.com.abril.nds.util.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Export {

	String label();
	
	Alignment alignment() default Alignment.LEFT;
	
	String alignWithHeader() default "";
	
	int exhibitionOrder() default 1000;
	
	boolean printVertical() default false;
	
	String propertyToDynamicLabel() default "";
	
	float widthPercent() default 0f;
	
	ColumnType columnType() default ColumnType.STRING;
	
	float fontSize() default 0;
	
	boolean xlsAutoSize() default false;
	
	public enum Alignment {
		
		LEFT(0),
		RIGHT(2),
		CENTER(1);
		
		private int value;
		
		private Alignment(int value) {
			
			this.value = value;
		}
		
		public int getValue() {
			
			return this.value;
		}
		
	}
	
}
