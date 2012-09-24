package br.com.abril.nds.integracao.engine.test;

import java.util.concurrent.atomic.AtomicReference;

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
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	public void processMessage(Message message) {
		System.out.println(message);
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
