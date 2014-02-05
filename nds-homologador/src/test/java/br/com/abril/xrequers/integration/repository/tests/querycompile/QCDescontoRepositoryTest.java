package br.com.abril.xrequers.integration.repository.tests.querycompile;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.repository.DescontoRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCDescontoRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private DescontoRepository descontoRepository;
	
	@Test
	public void test_buscar_produtos_edicoes_que_usam_desconto_produto(){
	
		Desconto desconto = new Desconto();
		desconto.setId(1L);
		
		List<Long> idsProdutosEdicao = descontoRepository.buscarProdutosEdicoesQueUsamDescontoProduto(desconto);
		
	}
	
	@Test
	public void test_buscar_produtos_que_usam_desconto_produto() {

		Desconto desconto = new Desconto();
		desconto.setId(1L);
		
		List<Long> idsProduto = descontoRepository.buscarProdutosQueUsamDescontoProduto(desconto);
		
	}
	
	@Test
	public void test_buscar_proximos_lancamentos_que_usam_desconto_produto() {

		Desconto desconto = new Desconto();
		desconto.setId(1L);
		
		List<Long> idsDescontoProximosLancamento = descontoRepository.buscarProximosLancamentosQueUsamDescontoProduto(desconto);
		
	}
	
	

}
