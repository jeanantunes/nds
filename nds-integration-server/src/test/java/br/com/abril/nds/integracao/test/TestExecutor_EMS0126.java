package br.com.abril.nds.integracao.test;

import org.junit.Test;

import br.com.abril.nds.integracao.fileimporter.InterfaceExecutor;
import br.com.abril.nds.integracao.model.InterfaceEnum;

public class TestExecutor_EMS0126 {

	@Test
	public void testarExecucaoInterface() {
		
		try {
			InterfaceExecutor executor = new InterfaceExecutor();
			executor.executarInterface("Jones", InterfaceEnum.EMS0126);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
