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

@Resource
@Path("/dataholder")
public class DataHolderController {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private Result result;

	@Post
	public void hold(String actionKey, String dataKey, String fieldKey, String fieldValue) {
		
		DataHolder dataHolder =
			(DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder == null) {
			
			dataHolder = new DataHolder();
		}
		
		Map<String, Map<String, Map<String, String>>> actionMap = dataHolder.getActionMap();
		
		if (actionMap == null) {
			
			actionMap = new HashMap<String, Map<String, Map<String, String>>>();
		}
		
		Map<String, Map<String, String>> actionDataMap = actionMap.get(actionKey);
		
		if (actionDataMap == null) {
			
			actionDataMap = new HashMap<String, Map<String, String>>();
		}
		
		Map<String, String> dataMap = actionDataMap.get(dataKey);
		
		if (dataMap == null) {
			
			dataMap = new HashMap<String, String>();
		}

		dataMap.put(fieldKey, fieldValue);
		
		dataHolder.setActionMap(actionMap);
		
		actionMap.put(actionKey, actionDataMap);
		
		actionDataMap.put(dataKey, dataMap);
		
		this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
		
		result.use(Results.json()).from("").serialize();
	}
	
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
		
		result.use(Results.json()).from("").serialize();
	}
	
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
		
		result.use(Results.json()).from("").serialize();
	}
	
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
		
		result.use(Results.json()).from("").serialize();
	}
	
}
