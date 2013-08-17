package br.com.abril.nds.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public class PropertyLoader {

    private static Properties props = new Properties();

    public static PropertiesFactoryBean getPropertiesFactoryBean() {
	PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
	propertiesFactoryBean.setLocation(new ClassPathResource("query-estudo.properties"));
	return propertiesFactoryBean;
    }

    public static Properties getPropertiesQueryEstudo() {

	try {

	    if (props.isEmpty()) {
		props.load(ClassLoader.getSystemResourceAsStream("query-estudo.properties"));
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return props;
    }

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
