package br.com.abril.nds.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListUtils {


	@SuppressWarnings("all")
	public static void orderList(final String orderType,final String path,List<Object> list){
		
		if(path==null || path.length()==0)
				return;
		
		Collections.sort(list, new Comparator<Object>() {
			
			public int compare(Object o1, Object o2){
				Object valuePath1 = Util.getValuePath(o1, path);
				Object valuePath2 = Util.getValuePath(o2, path);
				
				Class clazz = valuePath1.getClass();
				
				Method method;
				int i =0;
				try {
					method = clazz.getMethod("compareTo", clazz);
					if(orderType.equalsIgnoreCase("asc")){
						i = (Integer)method.invoke(valuePath1, valuePath2);
					}
					else if(orderType.equalsIgnoreCase("desc")){
						i = (Integer)method.invoke(valuePath2, valuePath1 );
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException 
						| InvocationTargetException e) {
					e.printStackTrace();
				}
					
				return i;
			}
			
		});
	}
	
	
	
	
    
}
