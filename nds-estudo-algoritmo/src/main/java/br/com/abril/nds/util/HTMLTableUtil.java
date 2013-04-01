package br.com.abril.nds.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class HTMLTableUtil {

	@SuppressWarnings("unchecked")
	public static <T> String buildHTMLTable(T type) {
		StringBuffer sb = new StringBuffer("<table border='1' cellspacing='0' cellpadding='2'>");
		List<T> listObjects = new ArrayList<>();

		if (type instanceof List) {
			listObjects.addAll((List<? extends T>) type);
		} else {
			listObjects.add(type);
		}

		Class<? extends Object> clazz = listObjects.get(0).getClass();
		Method[] methods = clazz.getMethods();
		Map<String, Method> sortedMethods = new TreeMap<String, Method>();
		for (Method method : methods) {
			String name = method.getName();
			if ((name.startsWith("get") || name.startsWith("is"))
					&& !name.equalsIgnoreCase("getClass") && !name.equalsIgnoreCase("getCotas")) {
				sortedMethods.put(name.replaceFirst("is|get", ""), method);
			}
		}

		Map<String, Method> finalOrderMethods = new LinkedHashMap<String, Method>();
		if (sortedMethods.containsKey("Id")) {
			finalOrderMethods.put("Id", sortedMethods.remove("Id"));
		}
		if (sortedMethods.containsKey("Numero")) {
			finalOrderMethods.put("Numero", sortedMethods.remove("Numero"));
		}
		if (sortedMethods.containsKey("NumeroCota")) {
			finalOrderMethods.put("NumeroCota", sortedMethods.remove("NumeroCota"));
		}
		if (sortedMethods.containsKey("NumeroEdicao")) {
			finalOrderMethods.put("NumeroEdicao", sortedMethods.remove("NumeroEdicao"));
		}
		finalOrderMethods.putAll(sortedMethods);
		
		Set<Entry<String, Method>> headers = finalOrderMethods.entrySet();
		buildHTMLTableHeader(headers, sb);

		for (T rowObject : listObjects) {
			buildHTMLTableRow(headers, rowObject, sb);
		}
		
		sb.append("</table>");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T, K, V> void buildHTMLTableRow(Set<Entry<String, Method>> headers, T rowObject, StringBuffer sb) {
		sb.append("<tr>");
		for (Entry<String, Method> entry : headers) {
			sb.append("<td>");
			Object rowValue = null;
			try {
				rowValue = entry.getValue().invoke(rowObject);
				if (rowValue instanceof Map) {
					Map<K, V> tempMap = new HashMap<>();
					tempMap.putAll((Map<? extends K, ? extends V>) rowValue);
					rowValue = tempMap.values();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			sb.append(String.valueOf(rowValue));
			sb.append("</td>");
		}
		sb.append("</tr>");
	}

	private static void buildHTMLTableHeader(Set<Entry<String, Method>> headers, StringBuffer sb) {
		sb.append("<tr>");
		for (Entry<String, Method> entry : headers) {
			sb.append("<th>");
			sb.append(entry.getKey());
			sb.append("</th>");
		}		
		sb.append("</tr>");
	}
}
