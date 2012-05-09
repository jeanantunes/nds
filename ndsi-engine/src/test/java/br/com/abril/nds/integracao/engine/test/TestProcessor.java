package br.com.abril.nds.integracao.engine.test;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;

public class TestProcessor implements MessageProcessor {
	private static TestProcessor instance = new TestProcessor();
	
	public static TestProcessor getInstance() {
		return instance;
	}
	
	private TestProcessor() {
		
	}
	
	public void processMessage(Message message) {
		System.out.println(message);
	}
}
