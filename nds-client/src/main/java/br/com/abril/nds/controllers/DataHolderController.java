package br.com.abril.nds.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.DataHolder;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsible for the interface of the data holder.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/dataholder")
public class DataHolderController {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private Result result;
	
	/**
	 * Hold the values in the data holder.
	 * 
	 * @param actionKey - the action key
	 * @param dataKey - the data key
	 * @param fieldKey - the field key
	 * @param fieldValue - the field value
	 */
	@Post
	public void hold(String actionKey, String dataKey, String fieldKey, String fieldValue) {
		
		DataHolder dataHolder = this.getCurrentDataHolder();
		
		Map<String, Map<String, Map<String, String>>> actionMap = this.getActionMap(dataHolder);
		
		Map<String, Map<String, String>> actionDataMap = this.getActionDataMap(actionKey, actionMap);
		
		Map<String, String> dataMap = this.getDataMap(dataKey, actionDataMap);

		this.configureDataHolder(actionKey, dataKey, fieldKey, fieldValue, 
			dataHolder, actionMap, actionDataMap, dataMap);
		
		this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
		
		this.result.use(Results.json()).from("").serialize();
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

	/*
	 * Obtains the current data holder from session or create a new one.
	 */
	private DataHolder getCurrentDataHolder() {
		
		DataHolder dataHolder =
			(DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder == null) {
			
			dataHolder = new DataHolder();
		}
		
		return dataHolder;
	}
	
	/**
	 * Clears an action identified by its key.
	 * 
	 * @param actionKey - the action key
	 */
	@Post
	public void clearAction(String actionKey) {
		
		DataHolder dataHolder =
			(DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {
		
			Map<String, Map<String, Map<String, String>>> actionMap = dataHolder.getActionMap();
			
			if (actionMap != null) {
	
				actionMap.put(actionKey, null);
				
				this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
			}
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	/**
	 * Clears the data according to the action key and data key.
	 *  
	 * @param actionKey - the action key
	 * @param dataKey - the data key
	 */
	@Post
	public void clearData(String actionKey, String dataKey) {
		
		DataHolder dataHolder =
			(DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {
		
			Map<String, Map<String, Map<String, String>>> actionMap = dataHolder.getActionMap();
			
			if (actionMap != null) {
	
				Map<String, Map<String, String>> actionDataMap = actionMap.get(actionKey);
				
				if (actionDataMap != null) {
					
					actionDataMap.put(dataKey, null);
					
					this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
				}
			}
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	/**
	 * Clears all data of the data holder.
	 */
	@Post
	public void clearAll() {
		
		DataHolder dataHolder =
			(DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {
		
			Map<String, Map<String, Map<String, String>>> actionMap = dataHolder.getActionMap();
			
			if (actionMap != null) {
	
				actionMap.clear();
				
				this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
			}
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
}
