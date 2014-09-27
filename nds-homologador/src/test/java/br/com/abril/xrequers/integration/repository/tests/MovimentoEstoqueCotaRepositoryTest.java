package br.com.abril.xrequers.integration.repository.tests;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.xrequers.integration.service.tests.TestUtil;

public class MovimentoEstoqueCotaRepositoryTest extends AbstractRepositoryTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Test
	public void test_obter_valores_aplicados_produto_edicao() {
		
		Date dataOperacao = TestUtil.criarData(17, Calendar.SEPTEMBER, 2014);
		Integer numeroCota = 6563;
		Long idProdutoEdicao = 50367L;
		
		ValoresAplicados valores = movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(numeroCota, idProdutoEdicao, dataOperacao);
		
		Assert.assertNotNull(valores);
		
		//precoDesconto precoVenda  valorDesconto 
		//13.9300	  	19.9000	 	30.0000
	}
	
}
