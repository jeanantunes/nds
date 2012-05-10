package br.com.abril.nds.integracao.engine.data;

import java.util.HashMap;
import java.util.Map;

public class Message {
	private Map<String, Object> header = new HashMap<String, Object>();
	private Object body;

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
	
	@Override
	public String toString() {
		return String.format("%s\n%s", header, body);
	}
}
