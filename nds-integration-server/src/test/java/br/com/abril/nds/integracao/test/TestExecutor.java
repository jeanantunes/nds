package br.com.abril.nds.integracao.test;

import org.junit.Test;

import br.com.abril.nds.integracao.fileimporter.InterfaceExecutor;
import br.com.abril.nds.integracao.model.InterfaceEnum;

public class TestExecutor {

	@Test
	public void testarExecucaoInterface() {
		
		try {
			InterfaceExecutor executor = new InterfaceExecutor();
			executor.executarInterface("jonatas", InterfaceEnum.EMS0134);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
