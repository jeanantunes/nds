package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;
import br.com.abril.nds.repository.HistoricoDescontoProdutoEdicaoRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCHistoricoDescontoProdutoEdicaoRepositoryTest extends AbstractRepositoryTest {
	
	@Autowired
	private HistoricoDescontoProdutoEdicaoRepository historicoDescontoProdutoEdicaoRepository;
	
	@Test
	public void test_buscar_historico_por_desconto_e_produto() {
		
		Long idDesconto = 1L;
		
		Long idProdutoEdicao = 1L;
		
		HistoricoDescontoProdutoEdicao hdpe = historicoDescontoProdutoEdicaoRepository.buscarHistoricoPorDescontoEProduto(idDesconto, idProdutoEdicao);
		
		
	}
	
			
	
}
