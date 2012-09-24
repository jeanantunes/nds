package br.com.abril.nds.integracao.engine.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Message {
	private Map<String, Object> header = new HashMap<String, Object>();
	private Object body;
	private AtomicReference<Object> tempVar;

	public Map<String, Object> getHeader() {
		return header;
	}
	public void setHeader(Map<String, Object> header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}	
	public AtomicReference<Object> getTempVar() {
		return tempVar;
	}
	public void setTempVar(AtomicReference<Object> tempVar) {
		this.tempVar = tempVar;
	}
	@Override
	public String toString() {
		return String.format("%s\n%s", header, body);
	}
}
