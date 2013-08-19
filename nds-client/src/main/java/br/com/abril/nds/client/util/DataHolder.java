package br.com.abril.nds.client.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Value Object para armazenamento de dados entre requests.
 * 
 * @author Discover Technology
 *
 * @param <Id> - Tipo de dados para o Id do mapa
 * @param <Data> - Tipo de dados do mapa
 */
public class DataHolder implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 692055195707888227L;
	
	public static final String SESSION_ATTRIBUTE_NAME = "dataHolder";
	
	private Map<String, Map<String, Map<String, String>>> actionMap;
	
	/**
	 * Construtor padrão.
	 */
	public DataHolder() {
		
	}
	
	/**
	 * Obtém o dado do mapa de ações.
	 * 
	 * @param actionKey - chave da ação
	 * @param dataKey - chave do dado
	 * @param fieldKey - chave do campo
	 * 
	 * @return Dado
	 */
	public String getData(String actionKey, String dataKey, String fieldKey) {
		
		Map<String, Map<String, String>> actionDataMap = this.actionMap.get(actionKey);
		
		if (actionDataMap == null) {
			
			return null;
		}
		
		Map<String, String> dataMap = actionDataMap.get(dataKey);
		
		if (dataMap == null) {
			
			return null;
		}

		return dataMap.get(fieldKey);
	}

	/**
	 * @return the actionMap
	 */
	public Map<String, Map<String, Map<String, String>>> getActionMap() {
		return actionMap;
	}

	/**
	 * @param actionMap the actionMap to set
	 */
	public void setActionMap(Map<String, Map<String, Map<String, String>>> actionMap) {
		this.actionMap = actionMap;
	}

}
