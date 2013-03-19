package br.com.abril.nds.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    
    private static Properties props = new Properties();
    
    public static String getProperty(String key) {
	try {
	    if (props.isEmpty()) {
		props.load(ClassLoader.getSystemResourceAsStream("query-estudo.properties"));
	    }
	    return props.getProperty(key);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return "";
    }
}
