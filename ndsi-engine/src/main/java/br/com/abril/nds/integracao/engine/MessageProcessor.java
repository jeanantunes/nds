package br.com.abril.nds.integracao.engine;

import br.com.abril.nds.integracao.engine.data.Message;

public interface MessageProcessor {
	public void processMessage(Message message);
}
