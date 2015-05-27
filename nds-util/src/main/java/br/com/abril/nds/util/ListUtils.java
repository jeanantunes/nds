package br.com.abril.nds.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ListUtils.class);


	@SuppressWarnings("all")
	public static void orderList(final String orderType,final String path,List list){
		
		if(path==null || path.length()==0)
				return;
		
		Collections.sort(list, new Comparator<Object>() {
			
			public int compare(Object o1, Object o2){
				if(o1==null || o2==null)
						return 0;
				
				Object valuePath1 = Util.getValuePath(o1, path);
				Object valuePath2 = Util.getValuePath(o2, path);
				
				int i =0;
				
				try {
					
					if(valuePath1==null){
						Class class1 = ((Class)Util.getReturnTypePath(o1,path));
						valuePath1= class1.getConstructor(String.class).newInstance("0");
						
					}
					if(valuePath2==null){
						Class class2 = ((Class)Util.getReturnTypePath(o2,path));
						valuePath2= class2.getConstructor(String.class).newInstance("0");
					}
					
					Class clazz = valuePath1.getClass();
					
					Method method;
				
					method = clazz.getMethod("compareTo", clazz);
					if(orderType.equalsIgnoreCase("asc")){
						i = (Integer)method.invoke(valuePath1, valuePath2);
					}
					else if(orderType.equalsIgnoreCase("desc")){
						i = (Integer)method.invoke(valuePath2, valuePath1 );
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException 
						| InvocationTargetException | InstantiationException e) {
					LOGGER.error(e.getMessage(), e);
				}
					
				return i;
			}
			
		});
	}
	
	@SuppressWarnings("all")
	public static void orderList(final String orderType,final String path,List list,final Class castClazz){
		
		if(path==null || path.length()==0)
				return;
		
		Collections.sort(list, new Comparator() {
			
			public int compare(Object o1, Object o2){
				Object valuePath1 = Util.getValuePath(o1, path);
				Object valuePath2 = Util.getValuePath(o2, path);
				
				if(valuePath1 instanceof String && (((String)valuePath1)==null || ((String)valuePath1).length()==0)){
					valuePath1="0";
				}
				if(valuePath2 instanceof String && (((String)valuePath2)==null || ((String)valuePath2).length()==0)){
					valuePath2="0";
				}
				
				Class clazz = castClazz;
				
				int i =0;
				try {
					
				Object obj1 = clazz.getConstructor(String.class).newInstance(valuePath1);
				Object obj2 = clazz.getConstructor(String.class).newInstance(valuePath2);
				
				Method method;
				
					method = clazz.getMethod("compareTo", clazz);
					if(orderType.equalsIgnoreCase("asc")){
						i = (Integer)method.invoke(obj1, obj2);
					}
					else if(orderType.equalsIgnoreCase("desc")){
						i = (Integer)method.invoke(obj2, obj1 );
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException 
						| InvocationTargetException | InstantiationException e) {
					LOGGER.error(e.getMessage(), e);
				}
					
				return i;
			}
			
		});
	}
	
	@SuppressWarnings("all")
	public static List getValuePathList(final String path,List list){
		List valueList = new ArrayList();
		
		for (Object object : list) {
			Object valuePath1 = Util.getValuePath(object, path);
			valueList.add(valuePath1);
		}
		
		
		return valueList;
		
	}
	
    
}
