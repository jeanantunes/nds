package br.com.abril.nds.integracao.test;

import org.junit.Test;

import br.com.abril.nds.integracao.route.RouteTemplate;

public abstract class RouteTestTemplate extends TestTemplate {
	/**
	 * Obtem a Rota a ser testada
	 * 
	 * @return Implementacao da rota
	 */
	public abstract RouteTemplate getRoute();
	
	@Test
	//@Rollback(false)
	public void test() {
		getRoute().execute("test");
	}
}
