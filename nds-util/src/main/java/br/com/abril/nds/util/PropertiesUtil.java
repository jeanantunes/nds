package br.com.abril.nds.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitária para leitura de arquivos de propriedades (.properties).
 * 
 * @author Discover Technology
 */
public class PropertiesUtil {

	/**
	 * Arquivo de propriedades carregado.
	 */
	private Properties properties = null;
	
	/**
	 * Construtor que indica qual o arquivo de propriedades a ser utilizado
	 */
	public PropertiesUtil(String propertyFileName)  {

		if (properties == null ) {
			
			properties = loadPropertyFile(propertyFileName);
		}
		
	}
	
	/**
	 * Método que retorna o valor de uma chave informada.
	 * 
	 * @param propertyName - nome da propriedade.
	 * 
	 * @return Valor da propriedade
	 */
	public String getPropertyValue(String propertyName) {
		
		return properties.getProperty(propertyName);
		
	}
	
	/**
	 * Método que carrega o arquivo de propriedades.
	 * 
	 * @return {@link Properties}
	 */
	private static Properties loadPropertyFile(String propFileName) {

		InputStream inputStream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
		
		Properties prop = new Properties();
		
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			return null;
		}

		return prop;

	}
	
}
