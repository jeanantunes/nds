package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.DescontoProdutoEdicaoExcessaoRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCDescontoProdutoEdicaoExcessaoRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private DescontoProdutoEdicaoExcessaoRepository descontoProdutoEdicaoExcessaoRepository;
	
	
	@Test
	public void test_buscar_desconto_cota_produto_excessao() {
		
		descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
				TipoDesconto.ESPECIFICO, 
				null, null, null, null, 1L, 1L);
		
		
	}
	
	
	
}
