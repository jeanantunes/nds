package br.com.abril.nds.integracao.test;

import org.junit.Test;

import br.com.abril.nds.integracao.fileimporter.InterfaceExecutor;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

public class TestExecutor_EMS0114 {

	@Test
	public void testarExecucaoInterface() {
		
		try {
			InterfaceExecutor executor = new InterfaceExecutor();
			executor.executarInterface("erick", InterfaceEnum.EMS0114);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}