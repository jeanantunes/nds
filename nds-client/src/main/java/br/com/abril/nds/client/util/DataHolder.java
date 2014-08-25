package br.com.abril.nds.client.util;

import java.io.Serializable;
import java.util.HashMap;
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
	
	
	public void hold (String actionKey, String dataKey, String fieldKey, String fieldValue, DataHolder dataHolder){
		
		Map<String, Map<String, Map<String, String>>> actionMap = this.getActionMap(dataHolder);
		
		Map<String, Map<String, String>> actionDataMap = this.getActionDataMap(actionKey, actionMap);
		
		Map<String, String> dataMap = this.getDataMap(dataKey, actionDataMap);

		this.configureDataHolder(actionKey, dataKey, fieldKey, fieldValue, dataHolder, actionMap, actionDataMap, dataMap);
	}
	
	
	
	/*
	 * Configures the data holder with the values.
	 */
	private void configureDataHolder(String actionKey, 
									 String dataKey,
									 String fieldKey, 
									 String fieldValue, 
									 DataHolder dataHolder,
									 Map<String, Map<String, Map<String, String>>> actionMap,
									 Map<String, Map<String, String>> actionDataMap,
									 Map<String, String> dataMap) {
		
		dataMap.put(fieldKey, fieldValue);
		
		actionDataMap.put(dataKey, dataMap);
		
		actionMap.put(actionKey, actionDataMap);
		
		dataHolder.setActionMap(actionMap);
	}

	/*
	 * Obtains the data map from the action data map using the data key.
	 */
	private Map<String, String> getDataMap(String dataKey,
										   Map<String, Map<String, String>> actionDataMap) {
		
		Map<String, String> dataMap = actionDataMap.get(dataKey);
		
		if (dataMap == null) {
			
			dataMap = new HashMap<String, String>();
		}
		
		return dataMap;
	}

	/*
	 * Obtains the action data map from the action map using the action key.
	 */
	private Map<String, Map<String, String>> getActionDataMap(String actionKey, 
															  Map<String, Map<String, Map<String, String>>> actionMap) {
		
		Map<String, Map<String, String>> actionDataMap = actionMap.get(actionKey);
		
		if (actionDataMap == null) {
			
			actionDataMap = new HashMap<String, Map<String, String>>();
		}
		
		return actionDataMap;
	}

	/*
	 * Obtains the action map from the data holder.
	 */
	private Map<String, Map<String, Map<String, String>>> getActionMap(DataHolder dataHolder) {
		
		Map<String, Map<String, Map<String, String>>> actionMap = dataHolder.getActionMap();
		
		if (actionMap == null) {
			
			actionMap = new HashMap<String, Map<String, Map<String, String>>>();
		}
		
		return actionMap;
	}


}
