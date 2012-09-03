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
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
	public void processMessage(Message message) {
		System.out.println(message);
	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void posProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
}
