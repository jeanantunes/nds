package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

//TODO: Implementar setup do teste para popular a base e realizar o teste efetivamente
@Ignore
public class ParametroSistemaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ParametroSistemaRepository psRepository;
	
	@Before
	public void setup() {
	}
	
	
	/** Verifica se o método retorna a collection de Parametros de sistema.  */
	@Test
	public void testBuscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = psRepository.buscarParametroSistemaGeral();
		Assert.assertNotNull(lst);
	}
	
	/** Verifica se traz a quantidade correta de parametros do sistema. */
	@Test
	public void testBuscarQuantidadeCorretaParametroSistemaGeral() {
		
		List<ParametroSistema> lst = psRepository.buscarParametroSistemaGeral();
		Assert.assertEquals(lst.size(), 22);
	}
	
}
