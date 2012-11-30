package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

//TODO: Implementar setup do teste para popular a base e realizar o teste efetivamente

public class ParametroSistemaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ParametroSistemaRepository psRepository;
	
	@Before
	public void setup() {
	}
	
	
	@Test
	public void buscarParametroPorTipoParametro() {
		
		TipoParametroSistema tipo = TipoParametroSistema.CNPJ_PJ_IMPORTACAO_NRE;
		
		ParametroSistema parametroSistema = psRepository.buscarParametroPorTipoParametro(tipo);
	
	}
	
	/** Verifica se o método retorna a collection de Parametros de sistema.  */
	@Test
	public void testBuscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = psRepository.buscarParametroSistemaGeral();
		Assert.assertNotNull(lst);
	}
	
	@Test
	public void salvar() {
		
		ParametroSistema parametroSistema = new ParametroSistema();
		parametroSistema.setId(1L);
		parametroSistema.setTipoParametroSistema(TipoParametroSistema.CNPJ_PJ_IMPORTACAO_NRE);
		parametroSistema.setValor("");
	
		Set<ParametroSistema> parametroSistemas = new HashSet<ParametroSistema>();
		parametroSistemas.add(parametroSistema);
		
		psRepository.salvar(parametroSistemas);
	}
		
	
	
}
